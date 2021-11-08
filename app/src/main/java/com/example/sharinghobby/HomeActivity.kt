 package com.example.sharinghobby

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.sharinghobby.data.model.result.SearchResultEntity
import com.example.sharinghobby.databinding.ActivityHomeBinding
import com.example.sharinghobby.data.model.result.LocationLatLngEntity
import com.example.sharinghobby.utillity.RetrofitUtil
import com.example.sharinghobby.utillity.fcm.FCMUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_create_hobby.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main_drawer_header.view.*
import kotlinx.android.synthetic.main.activity_main_toolbar.*
import kotlinx.android.synthetic.main.activity_main_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_hobby_info.*
import kotlinx.android.synthetic.main.dialog_hobby_info.group_member_limit
import kotlinx.android.synthetic.main.dialog_hobby_info.group_title
import kotlinx.android.synthetic.main.dialog_hobby_info.view.*
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.lang.NumberFormatException
import kotlin.coroutines.CoroutineContext
import kotlin.math.*


 class HomeActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope{

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityHomeBinding
    private lateinit var map: GoogleMap
    private var currentSelectMarker: Marker? = null
    private var changedLocationMarker: Marker? = null
    private var hobbyMakerArr = arrayListOf<Marker>()
    private var groupMemberJoinList = arrayListOf<String>()
    private var groupMarkOwnList = arrayListOf<String>()
    private var groupMarkInList = arrayListOf<String>()
    private var groupMarkOwnTitleList = arrayListOf<String>()
    private var groupMarkInTitleList = arrayListOf<String>()

    private lateinit var searchResult: SearchResultEntity
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private lateinit var changedLocation: LatLng

    private var isGpsChecked: Boolean = false
    private var isLocationSelected: Boolean = false
    private var isDisplayHobbyMarkers: Boolean = false

    private var categoryNumber: String = ""
    private var userIndex: String = ""
    private var tokenNumber: String = ""

    private val fireBase = DBConnector()

    companion object {
        const val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY"
        const val CAMERA_ZOOM_LEVEL = 17f
        const val PERMISSION_REQUEST_CODE = 101
        const val SMALLGROUP_MARK_LIMIT = 5 // 소모임 즐겨찾기 제한

        const val REQUEST_LOCATION = 10001
        const val REQUEST_CATEGORY = 10002
        const val REQUEST_CREATE_HOBBY = 10003

        const val DISTANCE_LIMIT = 2000 // 특정범위(2km)
        const val RADIUS = 6372.8 * 1000
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        /*val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        tokenNumber = pref.getString("token","")!!
        Log.e("token", tokenNumber)*/
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userIndex = intent.getStringExtra("uid")!!
        FCMUtil.getToken()

        val findHobby = Intent(this, CategoryActivity1::class.java)
        val createHobby = Intent(this, CreateHobbyActivity::class.java)
        val selectLocation = Intent(this, SearchActivity::class.java)
        val myHobbyList = Intent(this, BelongActivity::class.java)
        var chatList = Intent(this, ChattingListView::class.java)
        myHobbyList.putExtra("uid",userIndex);
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.bejji)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        job = Job()

        isGpsChecked = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.e("isGPS", isGpsChecked.toString())
        Log.e("isLoc", isLocationSelected.toString())

        if(isGpsChecked){
            binding.currentLocationButton.visibility = View.VISIBLE
        }else{
            binding.currentLocationButton.visibility = View.INVISIBLE
        }

        setupGoogleMap()

       if (isGpsChecked && !isLocationSelected) {
            getMyLocation()
            bindViews()
        }
        else if(!isGpsChecked && !isLocationSelected) {
           startActivityForResult(selectLocation, REQUEST_LOCATION)
        }

        // 메뉴바 리스너
        binding.viewToolbar.menuButton.setOnClickListener{
            var user_image : String = ""

            CoroutineScope(Dispatchers.Main).launch {
                binding.navigationView.menu.clear()

                withContext(Dispatchers.IO){
                    val user_data = fireBase.getData<Account>(userIndex)
                    user_image = user_data!!.user_image
                    binding.drawerLayout.header_user_nickname.text = user_data.nickname
                    binding.drawerLayout.header_user_email.text = user_data.user_email

                    if(user_data.groupmark_own_list != null) {
                        groupMarkOwnList = user_data.groupmark_own_list
                    }
                    if(user_data.groupmark_in_list != null) {
                        groupMarkInList = user_data.groupmark_in_list
                    }

                    if(!groupMarkOwnList.isNullOrEmpty()){
                        for(item in groupMarkOwnList){
                            val group_own_data = fireBase.getData<SmallGroup>(item)
                            val group_own_title = group_own_data!!.title

                            groupMarkOwnTitleList.add(group_own_title)
                        }
                    }

                    if(!groupMarkInList.isNullOrEmpty()){
                        for(item in groupMarkInList){
                            val group_in_data = fireBase.getData<SmallGroup>(item)
                            val group_in_title = group_in_data!!.title

                            groupMarkInTitleList.add(group_in_title)
                        }
                    }
                }
                val groupMarkOwnMenu = binding.navigationView.menu.addSubMenu("내가 만든 모임")
                if(!groupMarkOwnList.isNullOrEmpty()) {
                    for (i in groupMarkOwnList.indices) {
                        groupMarkOwnMenu.add(0, i+1, 0, groupMarkOwnTitleList[i]).setIcon(android.R.drawable.star_on)
                    }
                }else{
                    groupMarkOwnMenu.add(0,0,0,"즐겨찾기한 모임이 없습니다.")
                }
                val groupMarkInMenu = binding.navigationView.menu.addSubMenu("내가 가입한 모임")
                if(!groupMarkInList.isNullOrEmpty()){
                    for (i in groupMarkInList.indices){
                        groupMarkInMenu.add(0, i+6, 0, groupMarkInTitleList[i]).setIcon(android.R.drawable.star_on)
                    }
                }else{
                    groupMarkInMenu.add(0,0,0,"즐겨찾기한 모임이 없습니다.")
                }

                if (user_image != "default_photo") {
                    setImageWithGlide(
                        user_image,
                        binding.root.context,
                        binding.drawerLayout.header_icon
                    )
                    user_image = ""
                }

                binding.drawerLayout.openDrawer(
                    GravityCompat.START
                )

            }
        }
        binding.chatButton.setOnClickListener {
            chatList =intent.putExtra("idx","idxnum");
            startActivity(chatList);
        }
        binding.viewToolbar.noticeButton.setOnClickListener{
            val noticeDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_notice,null)
            val dialogBuilder = AlertDialog.Builder(this)
                .setView(noticeDialogView)
            val noticeDialog = dialogBuilder.create()

            noticeDialog.show()

            noticeDialog.window?.setLayout(1000, 1500)
        }

        binding.viewToolbar.mypageButton.setOnClickListener {
            startActivity(Intent(this,  UserMyPage::class.java).apply {
                putExtra("uid", userIndex)
            })
        }
        // 네비게이션바 리스너
        binding.navigationView.setNavigationItemSelectedListener { MenuItem ->
            binding.drawerLayout.closeDrawers()
            if (MenuItem.itemId != 0) {
                if(MenuItem.itemId in 1..5){
                    startActivity(Intent(this, BelongSmallGroup::class.java).apply {
                        putExtra("gid", groupMarkOwnList[MenuItem.itemId - 1])
                    })
                }
                else if(MenuItem.itemId in 6..10){
                    startActivity(Intent(this, BelongSmallGroup::class.java).apply {
                        putExtra("gid", groupMarkInList[MenuItem.itemId - 6])
                    })
                }
            }
            return@setNavigationItemSelectedListener false
        }



        // 각 버튼 별로 클릭리스너
        binding.findCreateHobbyButton.setOnClickListener {

            val findCreateHobbyDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_findhobby,null)
            val dialogBuilder = AlertDialog.Builder(this)
                .setView(findCreateHobbyDialogView)
            val findHobbyDialog = dialogBuilder.create()

            findHobbyDialog.show()

            val findHobbyButton = findCreateHobbyDialogView.findViewById<Button>(R.id.findHobbyButton)
            findHobbyButton.setOnClickListener {
                if(currentSelectMarker != null || changedLocationMarker != null) {
                    startActivityForResult(findHobby, REQUEST_CATEGORY)
                    findHobbyDialog.dismiss()
                }else{
                    Toast.makeText(this,"위치를 선택한 후 취미 찾기를 진행해주세요.",Toast.LENGTH_SHORT).show()
                    findHobbyDialog.dismiss()
                 }
            }

            val createHobbyButton = findCreateHobbyDialogView.findViewById<Button>(R.id.createHobbyButton)
            createHobbyButton.setOnClickListener {
                startActivityForResult(createHobby,REQUEST_CREATE_HOBBY)
                findHobbyDialog.dismiss()
            }

        }

        binding.chooseLocationButton.setOnClickListener {
            startActivityForResult(selectLocation, REQUEST_LOCATION)
        }

        binding.myHobbyListButton.setOnClickListener {
            startActivity(myHobbyList)
        }

        binding.chatButton.setOnClickListener {
            if(Firebase.auth.currentUser!=null) {
                chatList.putExtra("roomID", "0SSS4Q0RaA7urbOQKHoo")
                chatList.putExtra("UID", userIndex)
                startActivity(chatList)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_CATEGORY -> {
                if(resultCode == RESULT_OK){
                    isDisplayHobbyMarkers = true
                    categoryNumber = data?.getStringExtra("categoryNumber")!!
                    displayMarkers(categoryNumber)
                }
            }

            REQUEST_CREATE_HOBBY -> {
                if(resultCode == RESULT_OK){
                    // 선택한 카테고리 모임들 최신화
                    if(isDisplayHobbyMarkers){
                        displayMarkers(categoryNumber)
                    }

                    // 정상적으로 취미모임 만들어지면 .. -> DB에 값넣기
                    var fireBase = DBConnector();
                    var smallGroupData = SmallGroup(data?.getStringExtra("groupTitle")!!,
                    data?.getStringExtra("groupCategory")!!,
                    data?.getStringExtra("groupMemberLimit")!!,
                        userIndex, data?.getStringExtra("groupLocationLat")!!,
                        data?.getStringExtra("groupLocationLon")!!,
                        groupMemberJoinList,
                        data?.getStringExtra("groupIntro")!!,
                        "default_photo"
                    )
                    fireBase.setSmallGroupData(smallGroupData);
                }
            }

            REQUEST_LOCATION -> {
                if(resultCode == RESULT_OK){
                    isLocationSelected = true
                    if (currentSelectMarker != null  ){
                        currentSelectMarker?.remove()
                    }
                    if(changedLocationMarker != null ){
                        changedLocationMarker?.remove()
                    }
                    changedLocation = LatLng(
                        data?.getStringExtra("changedLocationLat")!!.toDouble(),
                        data?.getStringExtra("changedLocationLon")!!.toDouble()
                    )
                    setSelectedMarker()

                    if(isDisplayHobbyMarkers){
                        displayMarkers(categoryNumber)
                    }
                }
            }
        }
    }

    private fun bindViews() = with(binding) {
        currentLocationButton.setOnClickListener {
            isLocationSelected = false
            getMyLocation()
        }
    }

    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private fun setSelectedMarker(){

        var changedLocationLatLngEntity = LocationLatLngEntity(changedLocation.latitude.toFloat(), changedLocation.longitude.toFloat())
        changedLocationMarker = setupMarker(
            SearchResultEntity(
                fullAddress = "",
                name = "내 위치",
                locationLatLng = changedLocationLatLngEntity
            )
        )

        changedLocationMarker?.showInfoWindow()

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(changedLocation, CAMERA_ZOOM_LEVEL))

    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map

        map.setOnInfoWindowClickListener {
            clickMarker(it)
        }

    }

    private fun setupMarker(searchResult: SearchResultEntity): Marker {
        val positionLatLng = LatLng(
            searchResult.locationLatLng.latitude.toDouble(),
            searchResult.locationLatLng.lontitude.toDouble()
        )
        val markerOptions = MarkerOptions().apply {
            position(positionLatLng)
            title(searchResult.name)
            snippet(searchResult.fullAddress)
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))

        return map.addMarker(markerOptions)
    }

    private fun setupHobbyMarker(searchResult: SearchResultEntity): Marker {
        val hobbyPositionLatLng = LatLng(
            searchResult.locationLatLng.latitude.toDouble(),
            searchResult.locationLatLng.lontitude.toDouble()
        )
        val hobbyMarkerOptions = MarkerOptions().apply {
            position(hobbyPositionLatLng)
            title(searchResult.name)
            snippet(searchResult.fullAddress)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        return map.addMarker(hobbyMarkerOptions)
    }

    private fun getMyLocation() {
        if(changedLocationMarker != null ){
            changedLocationMarker?.remove()
        }

        if (::locationManager.isInitialized.not()) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsEnabled) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                setMyLocationListener()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L // 1.5초 최소시간
        val minDistance = 100f // 최소거리

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }
        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )

        }

    }

    private fun onCurrentLocationChanged(locationLatLngEntity: LocationLatLngEntity) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    locationLatLngEntity.latitude.toDouble(),
                    locationLatLngEntity.lontitude.toDouble()
                ), CAMERA_ZOOM_LEVEL
            )
        )
        loadReverseGeoInfo(locationLatLngEntity)
        removeLocationListener()
    }

    private fun loadReverseGeoInfo(locationLatLngEntity: LocationLatLngEntity) {

        if(!isLocationSelected) {
            launch(coroutineContext) {
                try {
                    withContext(Dispatchers.IO) {
                        val response = RetrofitUtil.apiService.getReverseGeoCode(
                            lat = locationLatLngEntity.latitude.toDouble(),
                            lon = locationLatLngEntity.lontitude.toDouble()
                        )
                        if (response.isSuccessful) {
                            val body = response.body()
                            withContext(Dispatchers.Main) {
                                //Log.e("list", body.toString())
                                body?.let {
                                    currentSelectMarker = setupMarker(
                                        SearchResultEntity(
                                            //fullAddress = it.addressInfo.fullAddress ?: "주소 정보 없음"
                                            fullAddress = "",
                                            name = "내 위치",
                                            locationLatLng = locationLatLngEntity
                                        )
                                    )
                                    currentSelectMarker?.showInfoWindow()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this@HomeActivity, "검색하는 과정에서 에러가 발생했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }else{
            var changedLocationLatLngEntity = LocationLatLngEntity(changedLocation.latitude.toFloat(), changedLocation.longitude.toFloat())
            changedLocationMarker = setupMarker(
                SearchResultEntity(
                    fullAddress = "",
                    name = "내 위치",
                    locationLatLng = changedLocationLatLngEntity
                )
            )

            changedLocationMarker?.showInfoWindow()

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(changedLocation, CAMERA_ZOOM_LEVEL))
        }
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                setMyLocationListener()
            } else {
                Toast.makeText(this, "권한을 받지 못했습니다.", Toast.LENGTH_SHORT).show()


            }
        }
    }

    private fun displayMarkers(categoryNumber: String){
        // 이미 카테고리선택 후, 다른 카테고리 선택시 지워주는 작업 + DB연동
        if(!hobbyMakerArr.isNullOrEmpty()) {

            // 기존에 있던 마커들 지우는 기능
            for (i in hobbyMakerArr.indices) {
                hobbyMakerArr[i].remove()
            }
            hobbyMakerArr.clear()
        }

        fireBase.db.collection("SmallGroup").whereEqualTo("category", categoryNumber).get().addOnSuccessListener {
            for(item in it.documents){
                var group_idx = item.id
                var node_title = item["title"]
                var node_lat = item["node_lat"]
                var node_lon = item["node_lon"]

                // 범위안에 있는지 체크
                var inRange = if(!isLocationSelected && isGpsChecked){
                     setDistanceRange(currentSelectMarker!!.position.latitude, currentSelectMarker!!.position.longitude,
                        node_lat.toString().toDouble(), node_lon.toString().toDouble())
                } else{
                    setDistanceRange(changedLocationMarker!!.position.latitude, changedLocationMarker!!.position.longitude,
                        node_lat.toString().toDouble(), node_lon.toString().toDouble())
                }

                if (inRange){
                    var hobbyLocationLatLngEntity = LocationLatLngEntity(node_lat.toString().toFloat(), node_lon.toString().toFloat())
                    var hobbyLocationMarker = setupHobbyMarker(
                        SearchResultEntity(
                            fullAddress = "",
                            name = node_title.toString(),
                            locationLatLng = hobbyLocationLatLngEntity
                        )
                    )
                    hobbyLocationMarker.tag = group_idx

                    hobbyMakerArr.add(hobbyLocationMarker)
                }
            }
        }
        fireBase.db.collection("SmallGroup").whereEqualTo("category", categoryNumber).get().addOnFailureListener {
            it.printStackTrace()
        }
    }

    private fun clickMarker(marker: Marker){

        if(marker in hobbyMakerArr) {
            val hobbyInfoDialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_hobby_info, null)
            val dialogBuilder = AlertDialog.Builder(this)
                .setView(hobbyInfoDialogView)
            val infoHobbyDialog = dialogBuilder.create()

            CoroutineScope(Dispatchers.Main).launch {
                Log.e("T1", Thread.currentThread().name)
                val tag = marker.tag.toString()
                var group_image: String = ""

                withContext(Dispatchers.IO) {
                    Log.e("T2", Thread.currentThread().name)
                    val data = fireBase.getData<SmallGroup>(tag)
                    group_image = data!!.photo
                    hobbyInfoDialogView.findViewById<TextView>(R.id.group_title).text = data!!.title
                    hobbyInfoDialogView.findViewById<TextView>(R.id.group_category).text =
                        data!!.category
                    hobbyInfoDialogView.findViewById<TextView>(R.id.group_member_limit).text =
                        data!!.user_limit
                    hobbyInfoDialogView.findViewById<TextView>(R.id.group_intro).text =
                        data!!.introduction
                }

                if(!group_image.isNullOrEmpty()) {
                    setImageWithGlide(
                        group_image,
                        hobbyInfoDialogView.context,
                        hobbyInfoDialogView.findViewById<ImageView>(R.id.group_image)
                    )
                    group_image = ""
                }


                Log.e("T3", Thread.currentThread().name)
                infoHobbyDialog.show()
                infoHobbyDialog.window?.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }

            val hobbyPageButton = hobbyInfoDialogView.findViewById<Button>(R.id.hobbyPageButton)
            // 자세히보기 버튼 -> 취미모임페이지로 이동
            hobbyPageButton.setOnClickListener {

                startActivity(Intent(this, BelongSmallGroup::class.java).apply {
                    putExtra("gid", marker.tag.toString())
                })

                infoHobbyDialog.dismiss()
        }

            val infoCanelButton = hobbyInfoDialogView.findViewById<Button>(R.id.infoCancelButton)
            infoCanelButton.setOnClickListener {
                infoHobbyDialog.dismiss()
            }
        }

    }

     // 특정범위 내에 존재하는지 확인
    private fun setDistanceRange(current_lat: Double, current_lon: Double, latitude: Double, longtitude: Double): Boolean{
         val dLat = Math.toRadians(latitude - current_lat)
         val dLon = Math.toRadians(longtitude - current_lon)
         val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(latitude)) * cos(Math.toRadians(current_lat))
         val c = 2 * asin(sqrt(a))

         var isLocatedIn = (RADIUS * c ) < DISTANCE_LIMIT


        return isLocatedIn
    }

    // 내 위치 리스너
    inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(location: Location) {
            val locationLatLngEntity = LocationLatLngEntity(
                location.latitude.toFloat(),
                location.longitude.toFloat()
            )
            onCurrentLocationChanged(locationLatLngEntity)
        }
    }
    // Url을 Glide를 이용하여 이미지 변환
     private fun setImageWithGlide(url:String, context:Context, imageLocation: ImageView){
         Glide.with(context)
             .load(url)
             .thumbnail(0.5f)
             .centerCrop()
             .into(imageLocation)
     }


}