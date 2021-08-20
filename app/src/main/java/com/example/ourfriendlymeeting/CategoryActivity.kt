package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ourfriendlymeeting.databinding.ActivityCategoryBinding
import com.google.android.material.tabs.TabLayoutMediator

class CategoryActivity : AppCompatActivity() {
    val binding by lazy{ActivityCategoryBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var  uid=intent.getIntExtra("from1",0)
        var locate =intent.getIntExtra("locate",0)
        val fragmentList = listOf(FragmentC(),FragmentD())
        val adapter = FindFragmentAdapter(this)
        adapter.fragmentlist = fragmentList
        binding.viewPager22.adapter=adapter
        val tabtables = listOf<String>("OUTdoor","INdoor")
        TabLayoutMediator(binding.tabLayout2,binding.viewPager22){tab,position->
            tab.text =tabtables[position]
        }.attach()
    }
    fun selectDone( kindOfHobby:String){
        val returnIntent = Intent()
        returnIntent.putExtra("returnV","$kindOfHobby");
        setResult(RESULT_OK,returnIntent)
        finish()
    }
}