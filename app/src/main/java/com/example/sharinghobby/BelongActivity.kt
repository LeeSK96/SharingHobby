package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityBelongBinding
import com.example.sharinghobby.view.adapter.BelongChartFragmentAdapter
import com.example.sharinghobby.view.fragment.FragmentBelong
import com.example.sharinghobby.view.fragment.FragmentMymade
import com.google.android.material.tabs.TabLayoutMediator

class BelongActivity : AppCompatActivity() {
    val binding by lazy{ActivityBelongBinding.inflate(layoutInflater)}
    var userIndex1="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(binding.root)

        val fragmentList = listOf(FragmentBelong(),FragmentMymade())
        val adapter =BelongChartFragmentAdapter(this)
        val userindex = intent.getStringExtra("uid")!!
        adapter.fragmentList = fragmentList
        binding.viewPager23.adapter = adapter
        userIndex1=userindex;
       // val bundle =Bundle()
        //bundle.putString("uid",userindex);
        //FragmentBelong.arguments= bundle;
        val tabTitle = listOf<String>("가입한모임","만든모임")
        TabLayoutMediator(binding.tabLayout23, binding.viewPager23){tab, position->
            tab.text =tabTitle[position]
        }.attach()
    }
    fun setFragment(){
        // return userIndex1;
    }
}