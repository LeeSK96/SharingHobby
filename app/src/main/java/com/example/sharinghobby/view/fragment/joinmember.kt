package com.example.sharinghobby.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.sharinghobby.ChatActivity
import com.example.sharinghobby.Memo1
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.FragmentJoinmemberBinding
import com.example.sharinghobby.databinding.FragmentTeamlistBinding
import com.example.sharinghobby.view.adapter.CustomAdapter
import com.example.sharinghobby.view.adapter.UserAcceptAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class joinmember(val gid:String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentJoinmemberBinding>(inflater, R.layout.fragment_joinmember, container, false)
        val adapter = UserAcceptAdapter(gid)
            /*CustomAdapter(onClickTask = {
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
             */
        binding.RecyclerViewjoin.adapter = adapter

        val data: MutableList<Memo1> = mutableListOf()
        Firebase.firestore.collection("SmallGroup").document(gid).get()
            .addOnSuccessListener {
                val groupmember_join_list: ArrayList<String> = it["groupmember_join_list"] as ArrayList<String>
                for (item in groupmember_join_list){
                    Log.e("uid", item)

                    if(!groupmember_join_list.isNullOrEmpty()) {
                        Firebase.firestore.collection("Users").document(item)
                            .get()
                            .addOnSuccessListener { userData ->
                                Log.e("userdata", userData.toString())
                                val newData: ArrayList<UserAcceptAdapter.Data> = arrayListOf()
                                newData.add(
                                    UserAcceptAdapter.Data(
                                        userData.id,
                                        userData["user_image"] as String,
                                        userData["nickname"] as String,
                                        false
                                    )
                                )
                                adapter.addData(newData)
                            }
                    }
                }
            }


        return binding.root
    }
}