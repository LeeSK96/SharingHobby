package com.example.sharinghobby.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sharinghobby.BelongSmallGroup
import com.example.sharinghobby.ChatActivity
import com.example.sharinghobby.Memo1
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.FragmentBelongBinding
import com.example.sharinghobby.databinding.FragmentTeamlistBinding
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class Teamlist(val gid:String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        val binding = DataBindingUtil.inflate<FragmentTeamlistBinding>(inflater, R.layout.fragment_teamlist, container, false)
        val adapter = CustomAdapter(onClickTask = {
            val intent = Intent(activity, ChatActivity::class.java )
            val myid = Firebase.auth.currentUser!!.uid
            val roomid = if (myid < it.idx) myid + "|" + it.idx
                         else               it.idx + "|" + myid
            intent.putExtra("roomID", roomid)
            intent.putExtra("UID",myid)
            //region 임시 코드: 개인 채팅방 접속 시 가입한 채팅방 로그 남기기
            Firebase.firestore.collection("Users").document(myid)
                .update(mapOf("private_chat_list."+it.idx to true))
            /*
            Firebase.firestore.collection("Users").document(myid)
                .get()
                .addOnSuccessListener {
                    for(item in (it["private_chat_list"] as Map<String,Boolean>?) ?: mapOf()){
                        item.key
                    }

                }
             */
            //endregion
            activity?.startActivity(intent)
        })
        binding.RecyclerView.adapter = adapter

        Firebase.firestore.collection("SmallGroup").document(gid).get()
            .addOnSuccessListener {
                CoroutineScope(Dispatchers.Default) .launch {
                    val data: MutableList<Memo1> = mutableListOf()
                    for (item in ((it["belong_user"]?: mapOf<String,Boolean>()) as Map<String,Boolean>)){
                        val docSnap = Firebase.firestore.collection("Users").document(item.key)
                            .get().await()
                        data.add(Memo1(url = docSnap["user_image"] as String, title = docSnap["nickname"] as String, timestamp = System.currentTimeMillis(),idx = docSnap.id))
                    }
                    adapter.setList(data)
                }
            }
        /*
        val data: MutableList<Memo1> = mutableListOf()
        Firebase.firestore.collection("Users").get()
            .addOnSuccessListener {
                for (item in it.documents){
                    data?.add(Memo1(url = item["user_image"] as String, title = item["nickname"] as String, timestamp = System.currentTimeMillis(),idx = item.id))//이거 임시 디폴트 넣은거 확인

                }
                adapter?.setList(data)
            }

        adapter?.setList(data)

         */

        return binding.root
    }

}