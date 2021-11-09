package com.example.sharinghobby

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.databinding.ActivityBelongSmallGroupBinding
import com.example.sharinghobby.view.adapter.BelongChartFragmentAdapter
import com.example.sharinghobby.view.fragment.*
import com.example.sharinghobby.view.gallaryActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_belong_small_group.*
import java.util.ArrayList
import com.example.sharinghobby.DBConnector
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class BelongSmallGroup : AppCompatActivity() {
    val url1: String =
        "https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021505.jpg?alt=media&token=03f946cf-ba7a-450c-a5d2-199dccae5f0b"
    val url2: String =
        "https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021427.jpg?alt=media&token=39e460f9-e36b-4361-8943-6d5bae87c829"
    val url3: String =
        "https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021403.jpg?alt=media&token=fe6d51ef-1430-44a2-8a7e-6a8f023c3750"
    val url4: String =
        "https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021313.jpg?alt=media&token=e53add8d-845e-493a-8ba8-be0bfdaadcd8"
    val url5: String =
        "https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021156.jpg?alt=media&token=ae490e40-6d4b-4e8f-a98e-082947ac7515"
    var urlList = ArrayList<String>()
    val binding by lazy { ActivityBelongSmallGroupBinding.inflate(layoutInflater) }
    lateinit var teamGallary: team_gallary
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        urlList.add(url1)
        urlList.add(url2)
        urlList.add(url3)
        urlList.add(url4)
        urlList.add(url5)
        val uid = Firebase.auth.currentUser!!.uid
        val gid: String? = if (intent.hasExtra("gid")) {
            intent?.getStringExtra("gid")
        } else null
        var lee: Boolean = false;
        var title = ""
        var groupImage = ""
        var master = ""
        CoroutineScope(Dispatchers.Default).launch {
            Firebase.firestore.collection("SmallGroup").document(gid!!)
                .get()
                .addOnSuccessListener {
                    title = it["introduction"].toString()
                    groupImage = it["photo"].toString()
                    master = it["master"].toString()

                    binding.GroupTitle.text = title
                    binding.master.text = master
                    setImageWithGlide1(groupImage)
                    var lee2:Boolean=false;
                    if (uid == master){
                        binding.button3.visibility = View.VISIBLE
                        lee2=true
                    }
                    lee = it["belong_user.$uid"] != null
                    Log.e("asdf", lee.toString())

                        if (lee || lee2) { //속해있으면
                            val fragmentList = listOf(team_notify(), team_gallary(), Teamlist(gid))
                            val adapter = BelongChartFragmentAdapter(this@BelongSmallGroup)

                            adapter.fragmentList = fragmentList
                            binding.viewPager24.adapter = adapter

                            binding.chattingButton.visibility = View.VISIBLE
                            //binding.joinButton.visibility = View.GONE
                            binding.chattingButton.setOnClickListener {
                                val intent = Intent(this@BelongSmallGroup, ChatActivity::class.java)
                                val myid = Firebase.auth.currentUser!!.uid
                                intent.putExtra("roomID", gid)
                                intent.putExtra("UID", myid)
                                startActivity(intent)
                            }
                        } else { // 아니면
                            val fragmentList = listOf(team_notify(), team_gallary())
                            val adapter = BelongChartFragmentAdapter(this@BelongSmallGroup)
                            adapter.fragmentList = fragmentList
                            binding.viewPager24.adapter = adapter

                            imageButton9.visibility = View.INVISIBLE
                            binding.chattingButton.visibility = View.VISIBLE
                            binding.joinButton.visibility = View.VISIBLE
                            binding.chattingButton.text = "소모임장과 채팅"
                            binding.chattingButton.setOnClickListener {
                                val intent = Intent(this@BelongSmallGroup, ChatActivity::class.java)
                                val myid = Firebase.auth.currentUser!!.uid
                                val roomid = if (myid < master) "$myid|$master"
                                else "$master|$myid"
                                intent.putExtra("roomID", roomid)
                                intent.putExtra("UID", myid)
                               // intent.putExtra("intro",title)
                                startActivity(intent)
                            }
                            binding.joinButton.setOnClickListener { _ ->
                                var isAlreadyDone = false
                                for (joinMember in (it["groupmember_join_list"] as ArrayList<String>)) {
                                    if (joinMember == uid) {
                                        isAlreadyDone = true
                                        break
                                    }
                                }
                                if (!isAlreadyDone) {
                                    Firebase.firestore.collection("SmallGroup").document(gid)
                                        .update("groupmember_join_list", FieldValue.arrayUnion(uid))
                                    Toast.makeText(
                                        this@BelongSmallGroup,
                                        "신청이 완료되었습니다. 승인을 기다려주세요.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@BelongSmallGroup,
                                        "이미 신청되어 있습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        val tabTitle = listOf<String>("팀게시글", "갤러리", "팀원목록")
                        TabLayoutMediator(
                            binding.tabLayout3,
                            binding.viewPager24
                        ) { tab, position ->
                            tab.text = tabTitle[position]
                        }.attach()
                    }



        }

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
        /*
        val fragmentList = listOf(team_notify(), team_gallary(), Teamlist(gid!!))
        val adapter = BelongChartFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager24.adapter = adapter

        val tabTitle = listOf<String>("팀게시글","갤러리","팀원목록")
        TabLayoutMediator(binding.tabLayout3, binding.viewPager24){tab, position->
            tab.text =tabTitle[position]
        }.attach()
         */
        // setFragment()
        // teamGallary.setValue(groupImage);
        binding.button3.setOnClickListener {
            val intent1 = Intent(this, Joinmembership::class.java)
            intent1.putExtra("gid", gid)
            startActivity(intent1);
        }
        binding.imageButton.setOnClickListener {
            val intent1 = Intent(this, MadeGroup2Activity::class.java)
            intent1.putExtra("groupId", 1)
            startActivityForResult(intent1, 99)
        }
        binding.bookmark.setOnClickListener {
            Toast.makeText(this, "즐겨찾기 추가!", Toast.LENGTH_LONG).show()

        }
    }

    /* fun setFragment(){
         val teamFragment = team_gallary()
         var bundle =Bundle()
         bundle.putStringArrayList("key1","")
         teamFragment.arguments =bundle;
         val transcation =supportFragmentManager.beginTransaction()
         transcation.add(R.id.frameLayout,teamFragment)
         transcation.commit()
     }*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                99 -> {
                    val image1: String? = data?.getStringExtra("returnUrl")
                    if (image1 == null) Toast.makeText(this, "1111", Toast.LENGTH_LONG).show()
                    if (image1 != null) {
                        setImageWithGlide1(image1)
                    }
                }
            }
        }
    }

    fun setImageWithGlide1(url: String) {
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(500, 500))
            .into(binding.imageButton)
    }

    fun goDetail(num: Int) {
        val intent1 = Intent(this, gallaryActivity::class.java)
        intent1.putStringArrayListExtra("urlcode", urlList)
        intent1.putExtra("code", num)
        startActivity(intent1)
    }
}