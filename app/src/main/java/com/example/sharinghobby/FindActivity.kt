package com.example.sharinghobby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sharinghobby.databinding.ActivityFindBinding
import com.google.android.material.tabs.TabLayoutMediator

class FindActivity : AppCompatActivity() {
    val binding by lazy { ActivityFindBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val fragmentList = listOf(FragmentA(),FragmentB())
        val adapter = FindFragmentAdapter(this)
        adapter.fragmentlist = fragmentList
        binding.viewPager2.adapter=adapter
        val tabtables = listOf<String>("ID찾기","PW찾기")
        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab,position->
            tab.text =tabtables[position]
        }.attach()
    }
    fun  showinfoId(accountId:String,chstate:Int){
        if(chstate==0){Toast.makeText(this,"Id는 ${accountId}입니다",Toast.LENGTH_LONG).show()
            binding.textView63.text = accountId}
        else{Toast.makeText(this,"PW는 ${accountId}입니다",Toast.LENGTH_LONG).show()
            binding.textView63.text = accountId}
    }

}