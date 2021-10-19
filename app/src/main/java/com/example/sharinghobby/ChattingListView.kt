package com.example.sharinghobby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityBelongBinding
import com.example.sharinghobby.databinding.ActivityChattingListViewBinding
import com.example.sharinghobby.view.adapter.BelongChartFragmentAdapter
import com.example.sharinghobby.view.fragment.FragmentBelong
import com.example.sharinghobby.view.fragment.FragmentMymade
import com.google.android.material.tabs.TabLayoutMediator

class ChattingListView : AppCompatActivity() {
    val binding by lazy{ ActivityChattingListViewBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_list_view)

        val fragmentList = listOf(FragmentBelong(), FragmentMymade())
        val adapter = BelongChartFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager25.adapter = adapter

        val tabTitle = listOf<String>("그룹채팅목록","개인채팅목록")
        TabLayoutMediator(binding.tabLayout4, binding.viewPager25){tab, position->
            tab.text =tabTitle[position]
        }.attach()
    }
}