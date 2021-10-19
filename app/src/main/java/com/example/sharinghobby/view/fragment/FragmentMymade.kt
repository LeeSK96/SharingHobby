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
import com.example.sharinghobby.databinding.FragmentMymadeBinding
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentMymade : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentMymadeBinding>(inflater, R.layout.fragment_mymade, container, false)
        val adapter = CustomAdapter(onClickTask = {
            val intent = Intent(activity, BelongSmallGroup::class.java )
            //intent.putExtra("memo", it)
            intent.putExtra("gid",it.idx)
            activity?.startActivity(intent)
        })
        binding.RecyclerViewMymade.adapter = adapter

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