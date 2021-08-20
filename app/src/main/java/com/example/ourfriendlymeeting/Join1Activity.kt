package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.ourfriendlymeeting.databinding.ActivityJoin1Binding

class Join1Activity : AppCompatActivity() {
    val binding by lazy { ActivityJoin1Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var checkID=false;
        var gender=""
        var data = listOf("성별선택","남자","여자")
        var adapter =ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data)
        binding.spinner.adapter=adapter
        binding.spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender=data[position]
                Log.d("from1",gender)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.checkId.setOnClickListener {
            //if (데이터베이스에서 가져올것 == binding.accountId.text.tostring()){토스트 메세지 출력 이미 있는 아이디 }
            //else checkID=true
            var userId ="dlwodb95"
            if(userId==binding.accountId.text.toString()) {Toast.makeText(this, "중복된 아이디입니다", Toast.LENGTH_SHORT)
                .show(); binding.checkId.text="중복확인"}
            else{checkID=true; binding.checkId.text="OK!!"}
        }
        binding.joinus.setOnClickListener {
            //if(!checkID)
            //if(gender)
            Toast.makeText(this, "회원가입완료!", Toast.LENGTH_SHORT).show()


          val returnIntent = Intent()
            returnIntent.putExtra("from1","1")
            setResult(RESULT_OK,returnIntent)
            finish()
        }
    }
}