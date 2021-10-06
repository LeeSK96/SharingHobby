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
    val url1:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021505.jpg?alt=media&token=03f946cf-ba7a-450c-a5d2-199dccae5f0b"
    val url2:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021427.jpg?alt=media&token=39e460f9-e36b-4361-8943-6d5bae87c829"
    val url3:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021403.jpg?alt=media&token=fe6d51ef-1430-44a2-8a7e-6a8f023c3750"
    val url4:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021313.jpg?alt=media&token=e53add8d-845e-493a-8ba8-be0bfdaadcd8"
    val url5:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021156.jpg?alt=media&token=ae490e40-6d4b-4e8f-a98e-082947ac7515"
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
                //.apply(RequestOptions().override(130,150))
                .into(binding.imageButton1)
        }
        fun setImageWithGlide2(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                //.apply(RequestOptions().override(130,150))
                .into(binding.imageButton2)
        }
        fun setImageWithGlide3(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
               // .apply(RequestOptions().override(130,150))
                .into(binding.imageButton3)
        }
        fun setImageWithGlide4(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
                //.apply(RequestOptions().override(130,150))
                .into(binding.imageButton4)
        }
        fun setImageWithGlide5(url:String){
            Glide.with(binding.root.context)
                .load(url)
                .thumbnail(0.5f)
                .centerCrop()
              //  .apply(RequestOptions().override(130,150))
                .into(binding.imageButton5)
        }
        fun setValue(value:String){
            setImageWithGlide1(url1)
            setImageWithGlide2(url2)
            setImageWithGlide3(url3)
            setImageWithGlide4(url4)
            setImageWithGlide5(url5)
        }

        setValue("go");
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