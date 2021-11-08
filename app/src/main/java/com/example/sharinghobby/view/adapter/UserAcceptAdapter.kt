package com.example.sharinghobby.view.adapter

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharinghobby.ChatActivity
import com.example.sharinghobby.DBConnector
import com.example.sharinghobby.R
import com.example.sharinghobby.databinding.ItemAcceptUserBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class UserAcceptAdapter(val gid: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data: ArrayList<Data> = arrayListOf()
    lateinit var mContext : Context

    fun addData(newData: ArrayList<Data>){
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return ViewHolder(ItemAcceptUserBinding.inflate(LayoutInflater.from(mContext),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as ViewHolder){
            data[position].let {
                binding.userName.text = it.userName
                binding.starred.setImageDrawable(mContext.getDrawable(
                    if (it.isStarred) R.drawable.ic_baseline_star_24
                    else              R.drawable.ic_baseline_star_border_24
                ))
                Glide.with(mContext)
                    .load(it.userImage)
                    .centerCrop()
                    .into(binding.userImage)

                binding.container.setOnClickListener { _ ->
                    val intent = Intent(mContext, ChatActivity::class.java )
                    val myid = Firebase.auth.currentUser!!.uid
                    val roomid = if (myid < it.UID) myid + "|" + it.UID
                    else               it.UID + "|" + myid
                    intent.putExtra("roomID", roomid)
                    intent.putExtra("UID",myid)


                    Firebase.firestore.collection("Users").document(myid)
                        .update(mapOf("private_chat_list."+it.UID to false))
                    mContext.startActivity(intent)
                }
                binding.acceptButton.setOnClickListener { _ ->
                    val db = DBConnector()
                    db.setBelongGroup(it.UID,gid)
                    db.setBelongUser(gid,it.UID)
                    Toast.makeText(mContext,"Accepted!!",Toast.LENGTH_SHORT).show()
                }

                binding.rejectButton.setOnClickListener{

                }
            }
        }
    }

    override fun getItemCount(): Int = data.size
    data class Data(val UID:String, val userImage : String, val userName:String,val isStarred:Boolean)
    inner class ViewHolder(val binding: ItemAcceptUserBinding) : RecyclerView.ViewHolder(binding.root)
}