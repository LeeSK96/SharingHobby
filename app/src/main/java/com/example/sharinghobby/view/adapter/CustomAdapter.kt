package com.example.sharinghobby.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.Memo1
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.ItemRecyclerBinding
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class CustomAdapter(var onClickTask : (memo : Memo1) -> Unit):RecyclerView.Adapter<Holder>() {
    private var listData = mutableListOf<Memo1>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //binding.bookmarker.setOnClickListener { Toast.makeText(parent.context,"${1}",Toast.LENGTH_LONG).show() }
        return Holder(binding).apply {
            this.onClickTask = this@CustomAdapter.onClickTask
            //this.bookmarker1.toString()
           }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       val memo =listData.get(position)
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setList(list : MutableList<Memo1>){
        CoroutineScope(Dispatchers.Main).launch {
            this@CustomAdapter.listData = list
            notifyDataSetChanged()
        }
    }

}

class Holder(val binding: ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root){
    lateinit var onClickTask : (memo : Memo1) -> Unit
    init {
        binding.bookmarker.setOnClickListener { Toast.makeText(binding.root.context,"${binding.textView69.text} 추가",Toast.LENGTH_LONG).show()
        }
    }
    //lateinit var bookmarker1 :(memo:Memo1)->Unit
    fun setMemo(memo: Memo1){
        binding.textView69.text = "${memo.title}"
        var sdf = SimpleDateFormat("yyyy/MM/dd")
        var formattedDate =sdf.format(memo.timestamp)
        binding.textView70.text = formattedDate
        binding.bookmarker.setImageDrawable(AppCompatResources.getDrawable(binding.root.context,
            if(memo.star) R.drawable.ic_baseline_star_24 else R.drawable.ic_baseline_star_border_24
        ))
        binding.container.setOnClickListener {
            onClickTask(memo)
           // Toast.makeText(binding.root.context,"${memo.title} ${memo.timestamp}",Toast.LENGTH_LONG).show()
        }
       /* binding.bookmarker.setOnClickListener {
            bookmarker1(memo)
        }*/
      //  if(memo.check>=10)
        //    binding.bookmarker.visibility = View.VISIBLE /*이부분 임시추가*/
       // else binding.bookmarker.visibility =View.GONE
        Glide.with(binding.root.context)
            .load(memo.url)
            .thumbnail(0.5f)
            .centerCrop()
            .into(binding.imageView4)
    }
}