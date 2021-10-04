package com.example.sharinghobby.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityGallaryBinding

class gallaryActivity : AppCompatActivity() {
    val binding by lazy { ActivityGallaryBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}