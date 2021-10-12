package com.example.sharinghobby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityBelongBinding
import com.example.sharinghobby.view.adapter.BelongChartFragmentAdapter
import com.example.sharinghobby.view.fragment.FragmentBelong
import com.google.android.material.tabs.TabLayoutMediator

class BelongActivity : AppCompatActivity() {
    val binding by lazy{ActivityBelongBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(binding.root)

        val fragmentList = listOf(FragmentBelong(),MyMadeFragment())
        val adapter =BelongChartFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager23.adapter = adapter

        val tabTitle = listOf<String>("가입한모임","만든모임")
        TabLayoutMediator(binding.tabLayout23, binding.viewPager23){tab, position->
            tab.text =tabTitle[position]
        }.attach()
    }
}