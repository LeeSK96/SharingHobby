package com.example.sharinghobby.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharinghobby.Memo1
import com.example.sharinghobby.databinding.ItemRecycler2Binding
import java.text.SimpleDateFormat

class CustomAdapter2(var onClickTask : (memo : Memo1) -> Unit): RecyclerView.Adapter<Holder2>() {
    private var listData = mutableListOf<Memo1>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val binding = ItemRecycler2Binding.inflate(LayoutInflater.from(parent.context),parent,false)
        //binding.bookmarker.setOnClickListener { Toast.makeText(parent.context,"${1}",Toast.LENGTH_LONG).show() }
        return Holder2(binding).apply {
            this.onClickTask = this@CustomAdapter2.onClickTask
            //this.bookmarker1.toString()
        }
    }

    override fun onBindViewHolder(holder: Holder2, position: Int) {
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

class Holder2(val binding: ItemRecycler2Binding): RecyclerView.ViewHolder(binding.root){
    lateinit var onClickTask : (memo : Memo1) -> Unit

    //lateinit var bookmarker1 :(memo:Memo1)->Unit
    fun setMemo(memo: Memo1){
        binding.textView69.text = "${memo.title}"
        var sdf = SimpleDateFormat("yyyy/MM/dd")
        var formattedDate =sdf.format(memo.timestamp)
        binding.textView70.text = formattedDate

        binding.container.setOnClickListener {
            onClickTask(memo)
            // Toast.makeText(binding.root.context,"${memo.title} ${memo.timestamp}",Toast.LENGTH_LONG).show()
        }
        /* binding.bookmarker.setOnClickListener {
             bookmarker1(memo)
         }*/

        if(memo.url != "default_photo") {
            Glide.with(binding.root.context)
                .load(memo.url)
                .thumbnail(0.5f)
                .centerCrop()
                .into(binding.imageView4)
        }
    }
}