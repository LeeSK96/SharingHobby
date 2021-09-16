package com.example.sharinghobby

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sharinghobby.databinding.ActivityHomeBinding
import com.example.sharinghobby.model.LocationLatLngEntity
import com.example.sharinghobby.model.SearchResultEntity
import com.example.sharinghobby.utillity.RetrofitUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.FirestoreGrpc
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HomeActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityHomeBinding
    private lateinit var map: GoogleMap
    private var currentSelectMarker: Marker? = null
    private lateinit var changedLocationMarker: MarkerOptions

    private lateinit var searchResult: SearchResultEntity
    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private lateinit var changedLocation: LatLng



    private var isGpsChecked: Boolean = false
    private var isLocationSelected: Boolean = false

    companion object {
        const val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY"
        const val CAMERA_ZOOM_LEVEL = 17f
        const val PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        isGpsChecked = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isGpsChecked) {
            setupGoogleMap()
            getMyLocation()
            bindViews()

        } else {
            setupGoogleMap()
        }
        val findHobby = Intent(this, FindHobbyActivity::class.java)
        val selectLocation = Intent(this, SearchActivity::class.java)
        val myHobbyList = Intent(this, MBselectedActivity::class.java)
        val chatList = Intent(this, ChatListActivity::class.java)
        val intent = Intent(this, ChatActivity::class.java)

        binding.findHobbyButton.setOnClickListener {
            startActivity(findHobby)
        }

        binding.chooseLocationButton.setOnClickListener {
            startActivity(selectLocation)
        }

        binding.myHobbyListButton.setOnClickListener {
            startActivity(myHobbyList)
        }

        /**
         * roomID -> 방 번호, UID -> 유저 ID
         */
        binding.chatButton.setOnClickListener {
            if(Firebase.auth.currentUser!=null) {
                startActivity(chatList)
                intent.putExtra("roomID", "room1")
                intent.putExtra("UID", Firebase.auth.currentUser!!.uid)
                this.startActivity(intent)
            }
        }
        if(intent.hasExtra("changedLocationLat") && intent.hasExtra("changedLocationLon")){
            isLocationSelected = true
        }

        if(isLocationSelected){
            // 수동 위치 설정
            changedLocation = LatLng(
                intent.getStringExtra("changedLocationLat")!!.toDouble(),
                intent.getStringExtra("changedLocationLon")!!.toDouble()
            )
        }



        /*if (::searchResult.isInitialized.not()){
            intent?.let{
                searchResult = it.getParcelableExtra(SEARCH_RESULT_EXTRA_KEY) ?: throw Exception("데이터가 존재하지 않습니다.")
                setupGoogleMap()
            }
        }
        bindViews()*/


    }

    private fun bindViews() = with(binding) {
        currentLocationButton.setOnClickListener {
            isLocationSelected = false
            changedLocationMarker.visible(false)
            getMyLocation()
        }
    }

    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        /*currentSelectMarker = setupMarker(searchResult)

        currentSelectMarker?.showInfoWindow()*/
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

        if(intent.hasExtra("categoryNumber")){
            displayMarkers()
        }


        return map.addMarker(markerOptions)
    }

    private fun getMyLocation() {
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
                                            fullAddress = it.addressInfo.fullAddress ?: "주소 정보 없음",
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
            changedLocationMarker = MarkerOptions().apply {
                position(changedLocation)
                title("내 위치")
                visible(true)
            }

            map.addMarker(changedLocationMarker)

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

    private fun displayMarkers(){
        val marker1 = MarkerOptions().apply {
            position(LatLng(36.61909677722212, 127.28567676157024))
            title("marker1")
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        }

        val marker2 = MarkerOptions().apply {
            position(LatLng(36.6188074837427, 127.28553003528177))
            title("marker2")
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        val marker3 = MarkerOptions().apply {
            position(LatLng(36.6189316496611, 127.2856831409741))
            title("marker3")
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        val marker4 = MarkerOptions().apply {
            position(LatLng(36.619177711935144, 127.28553840802694))
            title("marker4")
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        }

        val markerArr = arrayListOf<MarkerOptions>()
        markerArr.add(marker1)
        markerArr.add(marker2)
        markerArr.add(marker3)
        markerArr.add(marker4)

        for(i in markerArr.indices) {
            map.addMarker(markerArr[i])
        }

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

    // 정보창 클릭 리스너
    inner class InfoWindowActivity : AppCompatActivity(), GoogleMap.OnInfoWindowClickListener, OnMapReadyCallback{

        override fun onMapReady(map: GoogleMap) {
            map.setOnInfoWindowClickListener(this)
        }

        override fun onInfoWindowClick(marker: Marker) {
            Toast.makeText(this,"정보창 클릭 리스너",Toast.LENGTH_SHORT).show()
        }

    }

}

