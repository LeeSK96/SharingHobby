package com.example.sharinghobby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityBelongSmallGroupBinding
import com.example.sharinghobby.databinding.ActivityJoinmembershipBinding
import com.example.sharinghobby.view.adapter.BelongChartFragmentAdapter
import com.example.sharinghobby.view.fragment.joinmember
import com.example.sharinghobby.view.fragment.team_gallary
import com.example.sharinghobby.view.fragment.team_notify
import com.google.android.material.tabs.TabLayoutMediator

class Joinmembership : AppCompatActivity() {
    val binding by lazy{ ActivityJoinmembershipBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val gid = intent.getStringExtra("gid")
        val fragmentList = listOf(joinmember(gid!!))
        val adapter = BelongChartFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager26.adapter = adapter
        val tabTitle = listOf<String>("가입을 희망합니다!")
        TabLayoutMediator(binding.tabLayout5, binding.viewPager26){tab, position->
            tab.text =tabTitle[position]
        }.attach()
    }
}