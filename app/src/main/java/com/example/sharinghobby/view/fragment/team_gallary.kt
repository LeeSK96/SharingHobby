package com.example.sharinghobby.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.BelongActivity
import com.example.sharinghobby.BelongSmallGroup
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.FragmentTeamGallaryBinding

class team_gallary : Fragment() {
    var belongSmallGroupActivity: BelongSmallGroup? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding= FragmentTeamGallaryBinding.inflate(inflater,container,false)
        fun setImageWithGlide1(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                .apply(RequestOptions().override(500,500))
                .into(binding.imageButton1)
        }
        fun setImageWithGlide2(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                .apply(RequestOptions().override(500,500))
                .into(binding.imageButton2)
        }
        fun setImageWithGlide3(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                .apply(RequestOptions().override(500,500))
                .into(binding.imageButton3)
        }
        fun setImageWithGlide4(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                .apply(RequestOptions().override(500,500))
                .into(binding.imageButton4)
        }
        fun setImageWithGlide5(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                .apply(RequestOptions().override(500,500))
                .into(binding.imageButton5)
        }
        fun setValue(value:String){
            setImageWithGlide1(value)
            setImageWithGlide2(value)
            setImageWithGlide3(value)
            setImageWithGlide4(value)
            setImageWithGlide5(value)
        }
        binding.imageButton1.setOnClickListener{belongSmallGroupActivity?.goDetail(1)}
        binding.imageButton2.setOnClickListener{belongSmallGroupActivity?.goDetail(2)}
        binding.imageButton3.setOnClickListener{belongSmallGroupActivity?.goDetail(3)}
        binding.imageButton4.setOnClickListener{belongSmallGroupActivity?.goDetail(4)}
        binding.imageButton5.setOnClickListener{belongSmallGroupActivity?.goDetail(5)}
        binding.imageButton6.setOnClickListener{belongSmallGroupActivity?.goDetail(6)}
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is BelongSmallGroup)belongSmallGroupActivity=context
    }
}