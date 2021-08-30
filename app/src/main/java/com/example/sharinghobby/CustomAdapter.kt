package com.example.sharinghobby

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharinghobby.databinding.ActivityBelongBinding
import com.example.sharinghobby.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class CustomAdapter(val binding: ActivityBelongBinding,val context: Context,val belongActivity: BelongActivity):RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<Memo1>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      val binding =ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo =listData[position]
       // holder.
        //holder.setmemo1(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }


}

class Holder(val binding:ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root){

    init {

    }
    fun setmemo1(memo:Memo1){
        binding.textView69.text = "${memo.title}"
        var sdf =SimpleDateFormat("yyyy/MM/dd")
        var formattedDate = sdf.format(memo.timestamp)
        binding.textView70.text = formattedDate
    }

}