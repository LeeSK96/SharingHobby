package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ourfriendlymeeting.databinding.ActivitySearchAndMakeBinding

class SearchAndMake : AppCompatActivity() {
    val binding by lazy { ActivitySearchAndMakeBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var lst=intent.getIntExtra("location",0)
        val uid = intent.getIntExtra("from1",0)
        val intent1 = Intent(this,CategoryActivity::class.java)
        val intent2 =Intent(this,MadeGroupActivity::class.java)
        binding.button6.setOnClickListener {
             intent1.putExtra("from1",uid)
            intent1.putExtra("location",lst)
            startActivityForResult(intent1,99)
        }
        binding.button7.setOnClickListener {
            intent2.putExtra("from1",uid)
            intent2.putExtra("location",lst)
            startActivityForResult(intent2,199)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                 99->{ val returnIntent =Intent()
                       val Mycategory =data?.getStringExtra("returnV")
                     returnIntent.putExtra("returnS",Mycategory)
                     setResult(RESULT_OK,returnIntent)
                     finish()}
            }
        }
    }
}