package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.ourfriendlymeeting.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        thread(start=true){
        Thread.sleep(2000)
            runOnUiThread{
        showProgress(false)
            showButtons(true)
            Log.d("system111","메롱!")}
        }

        val intent1 = Intent(this,JoinActivity::class.java)
        val intent2 = Intent(this,FindActivity::class.java)
        val intent3 = Intent(this,CenterActivity::class.java)
        binding.joinbutton.setOnClickListener {
         startActivity(intent1)
        }
        binding.searchbutton.setOnClickListener {
            startActivity(intent2)
        }
        binding.loginbutton.setOnClickListener {
            intent.putExtra("uid",1)
            intent.putExtra("location",0)
            startActivity(intent3)
        }
    }
    fun showProgress(show:Boolean){
        if(show)binding.imageView5.visibility = View.VISIBLE
        else binding.imageView5.visibility=View.GONE
    }
    fun showButtons(show: Boolean){
        binding.loginbutton.visibility = View.VISIBLE
        binding.joinbutton.visibility = View.VISIBLE
        binding.searchbutton.visibility = View.VISIBLE
        binding.editTextTextPersonName.visibility = View.VISIBLE
        binding.editTextTextPersonName2.visibility = View.VISIBLE
    }
}