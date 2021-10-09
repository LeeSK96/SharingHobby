package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import java.util.ArrayList

class BelongSmallGroup : AppCompatActivity() {
    val url1:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021505.jpg?alt=media&token=03f946cf-ba7a-450c-a5d2-199dccae5f0b"
    val url2:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021427.jpg?alt=media&token=39e460f9-e36b-4361-8943-6d5bae87c829"
    val url3:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021403.jpg?alt=media&token=fe6d51ef-1430-44a2-8a7e-6a8f023c3750"
    val url4:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021313.jpg?alt=media&token=e53add8d-845e-493a-8ba8-be0bfdaadcd8"
    val url5:String="https://firebasestorage.googleapis.com/v0/b/ourfriendlymeetingtest1.appspot.com/o/image%2F20211006021156.jpg?alt=media&token=ae490e40-6d4b-4e8f-a98e-082947ac7515"
    var urlList=ArrayList<String>()
    val binding by lazy{ActivityBelongSmallGroupBinding.inflate(layoutInflater)}
    lateinit var teamGallary: team_gallary
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        urlList.add(url1)
        urlList.add(url2)
        urlList.add(url3)
        urlList.add(url4)
        urlList.add(url5)

        val gid = if (intent.hasExtra("gid")){
            intent.getStringExtra("gid")
        } else null

        var title = ""
        var groupImage = ""
        var master = ""
        Firebase.firestore.collection("SmallGroup").document(gid!!)
            .get()
            .addOnSuccessListener {
                title = it["introduction"].toString()
                groupImage = it["photo"].toString()
                master = it["master"].toString()

                binding.GroupTitle.text=title
                binding.master.text =master
                setImageWithGlide1(groupImage)
            }
        //여기 연결하는 방법 질문
        /*
        //val data: Memo1? =Groupinfo as Memo1
        //var title: String? =data?.title
       //var groupImage:String? =data?.url
        if (  title != null) {
          binding.GroupTitle.text=title.toString()
       }
        if (  groupImage != null) {
            setImageWithGlide1( groupImage)
        }
         */



        val fragmentList = listOf(team_notify(), team_gallary(), Teamlist())
        val adapter = BelongChartFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager24.adapter = adapter


        val tabTitle = listOf<String>("팀게시글","갤러리","팀원목록")
        TabLayoutMediator(binding.tabLayout3, binding.viewPager24){tab, position->
            tab.text =tabTitle[position]
        }.attach()

       // setFragment()
       // teamGallary.setValue(groupImage);
        binding.imageButton.setOnClickListener {
            val intent1 = Intent(this,MadeGroup2Activity::class.java)
            intent1.putExtra("groupId",1)
            startActivityForResult(intent1,99)
        }

        binding.chattingButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java )
            val myid = Firebase.auth.currentUser!!.uid
            intent.putExtra("roomID", gid)
            intent.putExtra("UID",myid)
            startActivity(intent)
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
        if(resultCode== RESULT_OK){
            when(requestCode){
                99->{val image1:String? =data?.getStringExtra("returnUrl")
                  if(image1==null) Toast.makeText(this,"1111",Toast.LENGTH_LONG).show()
                    if(image1!=null){
                    setImageWithGlide1(image1)
                    }
                }

            }
        }
    }
    fun setImageWithGlide1(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(500,500))
            .into(binding.imageButton)
    }
    fun goDetail(num:Int){
        val intent1 = Intent(this,gallaryActivity::class.java)
        intent1.putStringArrayListExtra("urlcode",urlList)
        intent1.putExtra("code",num)
        startActivity(intent1)
    }
}