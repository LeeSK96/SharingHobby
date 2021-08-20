package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ourfriendlymeeting.databinding.ActivityBelongBinding

class BelongActivity : AppCompatActivity() {
    val binding by lazy { ActivityBelongBinding.inflate(layoutInflater) }
    var uid:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        uid=intent.getIntExtra("from1",0)
        var data:MutableList<Memo1> =loadData()
        var adapter =CustomAdapter()
        adapter.listData =data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

    }

    fun loadData():MutableList<Memo1>{
        val data:MutableList<Memo1> = mutableListOf()
        for(no in 1..20){
            val title = "속한소모임$no"
            val date = System.currentTimeMillis()
            var memo1 = Memo1(no,title,date)
            data.add(memo1)
        };return data;
    }
}