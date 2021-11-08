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
import com.example.sharinghobby.databinding.FragmentChateamlistBinding
import com.example.sharinghobby.databinding.FragmentChatindivisualBinding
import com.example.sharinghobby.databinding.FragmentTeamlistBinding
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.example.sharinghobby.view.adapter.CustomAdapter2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatindivisualFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentChatindivisualBinding>(inflater, R.layout.fragment_chatindivisual, container, false)
        val adapter = CustomAdapter2(onClickTask = {
            val intent = Intent(activity, ChatActivity::class.java )
            val myid = Firebase.auth.currentUser!!.uid
            val roomid = if (myid < it.idx) myid + "|" + it.idx
            else               it.idx + "|" + myid
            intent.putExtra("roomID", roomid)
            intent.putExtra("UID",myid)
            Log.e("asdf","aiuwhefiowsjedfp0jawe0df")
            //region 임시 코드: 개인 채팅방 접속 시 가입한 채팅방 로그 남기기
            Firebase.firestore.collection("Users").document(myid)
                .update(mapOf("private_chat_list."+it.idx to true))
                .addOnFailureListener {
                    it.printStackTrace()
                }
                .addOnSuccessListener {
                    Log.e("asdf","LogSuccess")
                }
            //endregion
            activity?.startActivity(intent)
        })
        binding.RecyclerViewChatindiList.adapter = adapter

        val data: MutableList<Memo1> = mutableListOf()
        Firebase.firestore.collection("Users").get()
            .addOnSuccessListener {
                for (item in it.documents){
                    data?.add(Memo1(url = item["user_image"] as String, title = item["nickname"] as String, timestamp = System.currentTimeMillis(),idx = item.id,))

                }
                adapter?.setList(data)
            }

        adapter?.setList(data)

        return binding.root
    }
}