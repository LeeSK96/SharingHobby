package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.ourfriendlymeeting.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var connector = DBConnector()
        CoroutineScope(Dispatchers.Default).launch {
            var userData = connector.getData<Account>("651295262929965")
            if( userData != null) {
                // 여기서 작업 처리
            }
        }


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