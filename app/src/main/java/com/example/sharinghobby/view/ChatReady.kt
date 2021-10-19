package com.example.sharinghobby.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sharinghobby.ChatActivity
import com.example.sharinghobby.MadeGroup2Activity
import com.example.sharinghobby.databinding.ActivityBelongSmallGroupBinding
import com.example.sharinghobby.view.adapter.BelongChartFragmentAdapter
import com.example.sharinghobby.view.fragment.Teamlist
import com.example.sharinghobby.view.fragment.team_gallary
import com.example.sharinghobby.view.fragment.team_notify
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatReady : AppCompatActivity() {
    val binding by lazy{ ActivityBelongSmallGroupBinding.inflate(layoutInflater)}
    lateinit var teamGallary: team_gallary
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gid = if (intent.hasExtra("gid")){
            intent.getStringExtra("gid")
        } else null
        val intent = Intent(this, ChatActivity::class.java )
        val myid = Firebase.auth.currentUser!!.uid
        intent.putExtra("roomID", gid)
        intent.putExtra("UID",myid)
        startActivityForResult(intent,99)

        //여기 연결하는 방법 질문
        /*
        //val data: Memo1? =Groupinfo as Memo1
        //var title: String? =data?.title
       //var groupImage:String? =data?.url
        if (  title != null) {
          binding.GroupTitle.text=title.toString()
       }
        /**
         * UID1 UID2
         * UID1_UID2 <- 이 자체가 고유한 ID가 되는 것
         * 2명만 접속할 수 있는 채팅방이 되는 것
         * abc < abd
         */
        if (  groupImage != null) {
            setImageWithGlide1( groupImage)
        }
         */
    }
}