package com.example.sharinghobby.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.Memo1
import com.example.sharinghobby.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class CustomAdapter(var onClickTask : (memo : Memo1) -> Unit):RecyclerView.Adapter<Holder>() {
    private var listData = mutableListOf<Memo1>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding).apply {
            this.onClickTask = this@CustomAdapter.onClickTask }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       val memo =listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setList(list : MutableList<Memo1>){
        this.listData = list
        notifyDataSetChanged()
    }

}

class Holder(val binding: ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root){
    lateinit var onClickTask : (memo : Memo1) -> Unit

    fun setMemo(memo: Memo1){
        binding.textView69.text = "${memo.title}"
        var sdf = SimpleDateFormat("yyyy/MM/dd")
        var formattedDate =sdf.format(memo.timestamp)
        binding.textView70.text = formattedDate

        binding.container.setOnClickListener {
            onClickTask(memo)
           // Toast.makeText(binding.root.context,"${memo.title} ${memo.timestamp}",Toast.LENGTH_LONG).show()
        }
        Glide.with(binding.root.context)
            .load(memo.url)
            .thumbnail(0.5f)
            .centerCrop()
            .into(binding.imageView4)
    }
}