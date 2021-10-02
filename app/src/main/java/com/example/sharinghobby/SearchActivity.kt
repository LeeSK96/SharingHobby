package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.sharinghobby.MapActivity.Companion.SEARCH_RESULT_EXTRA_KEY
import com.example.sharinghobby.databinding.ActivitySearchBinding
import com.example.sharinghobby.data.model.result.LocationLatLngEntity
import com.example.sharinghobby.data.model.result.SearchResultEntity
import com.example.sharinghobby.data.model.search.Poi
import com.example.sharinghobby.data.model.search.Pois
import com.example.sharinghobby.utillity.RetrofitUtil
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext


class SearchActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SearchRecyclerAdapter

    companion object {
        const val REQUEST_LOCATION_OK = 10101
        const val REQUEST_LOCATION_HOBBY_OK = 10102
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("typeOfIntent")){
            binding.searchToolbar.toolbarTitle.text = "모임 위치를 입력해주세요."
        }

        job = Job()

        initAdapter()
        initViews()
        bindViews()
        initData()

        // 메뉴바 리스너
        binding.searchToolbar.backButton.setOnClickListener{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_LOCATION_OK -> {
                if (resultCode == RESULT_OK){
                    // click submit  > go to home
                    //    setResult()
                    val selectLocationIntent = Intent(this,HomeActivity::class.java).apply {
                        putExtra("changedLocationLat", data?.getStringExtra("changedLocationLat"))
                        putExtra("changedLocationLon", data?.getStringExtra("changedLocationLon"))
                    }
                    setResult(RESULT_OK, selectLocationIntent)
                    finish()
                }
            }

            REQUEST_LOCATION_HOBBY_OK -> {
                if(resultCode == RESULT_OK){
                    // create hobby > go to home
                    val createHobbyIntent = Intent(this, CreateHobbyActivity::class.java).apply {
                        putExtra("changedLocationLat", data?.getStringExtra("changedLocationLat"))
                        putExtra("changedLocationLon", data?.getStringExtra("changedLocationLon"))
                    }
                    setResult(RESULT_OK, createHobbyIntent)
                    finish()
                }
            }
        }


        if(requestCode == REQUEST_LOCATION_OK){
            if(resultCode == RESULT_OK){

            }
            // remain
        }
    }

    private fun initViews() = with(binding){
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding){
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun initAdapter(){
        adapter = SearchRecyclerAdapter()
    }

    private fun initData(){
        adapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois){
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name?: "빌딩명 없음",
                fullAddress = makeMainAdress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }
        adapter.setSearchResultList(dataList) {
            Toast.makeText(
                this,
                "빌딩이름 : ${it.name} 주소 : ${it.fullAddress} 위도/경도 : ${it.locationLatLng}",
                Toast.LENGTH_SHORT
            ).show()
            // home (data)  > search > map (data)
            // TODO
            startActivityForResult(Intent(this, MapActivity::class.java).apply {
                putExtra(SEARCH_RESULT_EXTRA_KEY, it)
                if (intent.hasExtra("typeOfIntent")){
                    putExtra("typeOfIntent", "createHobby")
                }

            }, REQUEST_LOCATION_OK)

        }
    }

    private fun searchKeyword(keywordString: String){
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO){
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keywordString
                    )
                    if(response.isSuccessful){
                        val body = response.body()
                        withContext(Dispatchers.Main){
                            Log.e("response", body.toString())
                            body?.let { searchResponse ->
                                setData(searchResponse.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(this@SearchActivity, "검색하는 과정에서 에러가 발생했습니다. : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeMainAdress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}