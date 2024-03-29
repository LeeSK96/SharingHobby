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
import com.example.sharinghobby.databinding.FragmentChateamlistBinding
import com.example.sharinghobby.databinding.FragmentMymadeBinding
import com.example.sharinghobby.view.ChatReady
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.example.sharinghobby.view.adapter.CustomAdapter2
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Chateamlist : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentChateamlistBinding>(inflater, R.layout.fragment_chateamlist, container, false)
        val adapter = CustomAdapter2(onClickTask = {
            val intent = Intent(activity, ChatReady::class.java )
            //intent.putExtra("memo", it)
            intent.putExtra("gid",it.idx)
            activity?.startActivity(intent)
        })
        binding.RecyclerViewChatList.adapter = adapter

        val data: MutableList<Memo1> = mutableListOf()
        Firebase.firestore.collection("SmallGroup").get()
            .addOnSuccessListener {
                for (item in it.documents){
                    data.add(Memo1(url = item["photo"] as String, title = item["introduction"] as String, idx = item.id,timestamp = System.currentTimeMillis()))
                }
                adapter.setList(data)
            }

        adapter.setList(data)

        return binding.root
    }
}