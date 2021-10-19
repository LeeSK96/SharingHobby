package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityLoginBinding
import com.example.sharinghobby.databinding.ActivityUserMyPageBinding

class UserMyPage : AppCompatActivity() {
    val binding by lazy{ ActivityUserMyPageBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imageButton7.setOnClickListener {
            val intent1 = Intent(this,MadeGroup2Activity::class.java)
            intent1.putExtra("groupId",1)
            startActivityForResult(intent1,99)
        }
    }
}