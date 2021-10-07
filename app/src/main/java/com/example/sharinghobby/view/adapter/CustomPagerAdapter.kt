package com.example.sharinghobby.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.databinding.ItemViewpagerBinding

class CustomPagerAdapter:RecyclerView.Adapter<Holder1>() {
    var imagelist = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder1 {
        val binding = ItemViewpagerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder1(binding)
    }

    override fun onBindViewHolder(holder: Holder1, position: Int) {
        val url= imagelist[position]
        holder.setImageWithGlide1(url)
    }

    override fun getItemCount(): Int {
       return imagelist.size
    }
}
class Holder1(val binding: ItemViewpagerBinding):RecyclerView.ViewHolder(binding.root){
    fun setImageWithGlide1(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
             //  .apply(RequestOptions().override(500,600))
            .into(binding.imageView)
    }

}