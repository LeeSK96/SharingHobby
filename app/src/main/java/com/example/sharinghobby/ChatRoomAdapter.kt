package com.example.sharinghobby

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharinghobby.ChatActivity
import com.example.sharinghobby.ChatRoomData
import com.example.sharinghobby.databinding.RecyclerChatlistBinding
import com.google.firebase.Timestamp

data class ChatRoomData(val room_id: String, val title: String, val recent_chat:String, val recent_datetime: Timestamp)
class ChatRoomAdapter(val mActivity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data: ArrayList<ChatRoomData> = arrayListOf()
    var user: String? = null
    fun addData(data:ArrayList<ChatRoomData>){
        this.data.addAll(data)
        data.sortByDescending { it.recent_datetime }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        = DefaultHolder(RecyclerChatlistBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as DefaultHolder){
            binding.Title.text = data[position].title
            binding.recentMessage.text = data[position].recent_chat
            binding.root.setOnClickListener {
                if(user!=null) {
                    val intent = Intent(mActivity, ChatActivity::class.java)
                    intent.putExtra("roomID", data[position].room_id)
                    intent.putExtra("UID", user)
                    mActivity.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = 0
    inner class DefaultHolder(val binding: RecyclerChatlistBinding) : RecyclerView.ViewHolder(binding.root)
}