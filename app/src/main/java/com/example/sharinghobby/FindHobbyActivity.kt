package com.example.sharinghobby

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.example.sharinghobby.databinding.ActivityFindMakeHobbyBinding

class FindHobbyActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFindMakeHobbyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindMakeHobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectHobby = Intent(this, CategoryActivity1::class.java)
        val MadeGroup =Intent(this,MadeGroupActivity::class.java)

        binding.selectHobbyButton.setOnClickListener {
            startActivity(selectHobby)
        }

        binding.makeHobbyButton.setOnClickListener {
            startActivity(MadeGroup)
        }
    }



}