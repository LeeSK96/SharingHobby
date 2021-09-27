package com.example.sharinghobby.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sharinghobby.BelongSmallGroup
import com.example.sharinghobby.Memo1
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.FragmentBelongBinding
import com.example.sharinghobby.view.adapter.CustomAdapter

class FragmentBelong : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentBelongBinding>(inflater, R.layout.fragment_belong, container, false)
        val adapter = CustomAdapter(onClickTask = {
            val intent = Intent(activity, BelongSmallGroup::class.java )
            intent.putExtra("memo", it)
            activity?.startActivity(intent)
        })
        binding.RecyclerView.adapter = adapter

        val data: MutableList<Memo1> = mutableListOf()
        for(no in 1..20){
            data.add(Memo1(title = "group:${no}", timestamp = System.currentTimeMillis()))
        }

        adapter.setList(data)

        return binding.root
    }

}