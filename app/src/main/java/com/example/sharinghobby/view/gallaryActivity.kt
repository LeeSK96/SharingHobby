package com.example.sharinghobby.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityGallaryBinding
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.example.sharinghobby.view.adapter.CustomPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class gallaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityGallaryBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val codeinfo = intent.getIntExtra("Intcode",0)
        val urlList=intent.getStringArrayListExtra("urlcode")
        val customAdapter =CustomPagerAdapter()
        customAdapter.imagelist = urlList!!
        binding.viewPager.adapter=customAdapter

    }
}