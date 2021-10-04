package com.example.sharinghobby

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import androidx.appcompat.app.AppCompatActivity
import com.example.sharinghobby.databinding.ActivityCreateHobbyBinding
import kotlinx.android.synthetic.main.activity_made_group.*

class CreateHobbyActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateHobbyBinding

    companion object{
        const val REQUEST_INFO = 10201
    }

    var title = ""
    var category = ""
    //val category_items = resources.getStringArray(R.array.category_array)
    var limit_user = ""
    var intro = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateHobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectHobbyLoc = Intent(this, SearchActivity::class.java)

        //val categoryAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, category_items)

        //binding.categorySpinner.adapter = categoryAdapter
        //binding.subcategorySpinner.adapter = categoryAdapter

        /*binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {

                    }
                    3 -> {

                    }
                    4 -> {

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }*/

        binding.checkButton.setOnClickListener {
            title = binding.createGroupTitle.text.toString()
            category = binding.createGroupCategory.text.toString()
            limit_user = binding.createGroupMemberLimit.text.toString()
            intro = binding.createGroupIntro.text.toString()

            selectHobbyLoc.putExtra("typeOfIntent", "createHobby")
            startActivityForResult(selectHobbyLoc, REQUEST_INFO)
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_INFO -> {
                val createIntent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("groupTitle", title)
                    putExtra("groupCategory", category)
                    putExtra("groupMemberLimit",limit_user)
                    putExtra("groupLocationLat", data?.getStringExtra("changedLocationLat"))
                    putExtra("groupLocationLon", data?.getStringExtra("changedLocationLon"))
                    putExtra("groupIntro", intro)
                    // 모임만드는데 필요한 정보들 더 추가해서 보내기
                }
                setResult(RESULT_OK, createIntent)
                finish()
            }
        }
    }
}