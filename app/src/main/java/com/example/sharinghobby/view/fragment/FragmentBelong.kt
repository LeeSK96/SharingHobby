package com.example.sharinghobby.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sharinghobby.*
import com.example.sharinghobby.databinding.FragmentBelongBinding
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FragmentBelong : Fragment() {
    var BelongActivity1: BelongActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val uid = Firebase.auth.currentUser!!.uid
        val binding = DataBindingUtil.inflate<FragmentBelongBinding>(inflater, R.layout.fragment_belong, container, false)
        val adapter = CustomAdapter(onClickTask = {
            val intent = Intent(activity, BelongSmallGroup::class.java )
            //intent.putExtra("memo", it)
            Log.d("from1","${uid}")
            intent.putExtra("gid",it.idx)
            intent.putExtra("uid",uid)
            activity?.startActivity(intent)
        })
        binding.RecyclerView.adapter = adapter

        Firebase.firestore.collection("Users").document(uid).get()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.Default) .launch {
                    val data: MutableList<Memo1> = mutableListOf()
                    for (item in ((it["belong_group" ]?: mapOf<String,Boolean>()) as Map<String,Boolean>)){
                        val docSnap = Firebase.firestore.collection("SmallGroup").document(item.key)
                            .get().await()
                        data.add(Memo1(url = docSnap["photo"] as String, title = docSnap["introduction"] as String, idx = docSnap.id,timestamp = System.currentTimeMillis(),star = item.value))
                    }
                    adapter.setList(data)
                }
            }




        return binding.root
    }

}