package com.example.sharinghobby.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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