package com.example.sharinghobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharinghobby.databinding.ActivityMapBinding
import com.example.sharinghobby.data.model.result.SearchResultEntity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MapActivity : AppCompatActivity(), OnMapReadyCallback, CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap
    private var currentSelectMarker: Marker? = null

    private lateinit var searchResult: SearchResultEntity

    private lateinit var changedStartLocation : LatLng
    private lateinit var changedDragLocation : LatLng
    private lateinit var changedFinalLocation : LatLng


    companion object {
        const val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY"
        const val CAMERA_ZOOM_LEVEL = 17f
        const val PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.searchToolbar.toolbarTitle.text = "현재위치로 마커를 드래그해주세요."

        job = Job()

        if (::searchResult.isInitialized.not()) {
            intent?.let {
                searchResult = it.getParcelableExtra(SEARCH_RESULT_EXTRA_KEY)
                    ?: throw Exception("데이터가 존재하지 않습니다.")
                setupGoogleMap()


            }
        }

        // 메뉴바 리스너
        binding.searchToolbar.backButton.setOnClickListener{
            finish()
        }

        binding.LocationCheckButton.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("changedLocationLat", changedFinalLocation.latitude.toString())
                putExtra("changedLocationLon", changedFinalLocation.longitude.toString())
            }
            setResult(RESULT_OK, intent)
            finish()

        }

        binding.LocationCancelButton.setOnClickListener {
            startActivity (
                Intent(this, HomeActivity::class.java)
            )
        }

    }


    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        currentSelectMarker = setupMarker(searchResult)

        currentSelectMarker?.showInfoWindow()

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
            draggable(true)
        }

        changedFinalLocation = positionLatLng

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))

        map.setOnMarkerDragListener(MarkerDragListener())

        return map.addMarker(markerOptions)
    }

    // 마커 드래그 리스너
    inner class MarkerDragListener : GoogleMap.OnMarkerDragListener{
        override fun onMarkerDragStart(marker: Marker) {
            changedStartLocation = marker.position
        }

        override fun onMarkerDrag(marker: Marker) {
            changedDragLocation = marker.position
        }

        override fun onMarkerDragEnd(marker: Marker) {
            changedFinalLocation = marker.position
        }
    }




}