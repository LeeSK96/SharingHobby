package com.example.SharingHobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ourfriendlymeeting.databinding.ActivityMbselectedBinding

class MBselectedActivity : AppCompatActivity() {
    val binding by lazy{ActivityMbselectedBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val uid = intent.getIntExtra("from1",0)
        val intent1= Intent(this,BelongActivity::class.java)
        val intent2= Intent(this,MadeActivity::class.java)
        binding.button3.setOnClickListener { intent1.getIntExtra("from1",uid); startActivity(intent1) }
        binding.button4.setOnClickListener { intent2.getIntExtra("from1",uid) }
    }
}