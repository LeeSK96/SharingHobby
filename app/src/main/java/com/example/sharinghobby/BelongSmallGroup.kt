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

class BelongSmallGroup : AppCompatActivity() {
    val binding by lazy{ActivityBelongSmallGroupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var Groupinfo = intent.getSerializableExtra("memo");
        //여기 연결하는 방법 질문
        val data: Memo1? =Groupinfo as Memo1
        var title: String? =data?.title
       var groupImage:String? =data?.url
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
        val fragmentList = listOf(team_notify(), team_gallary(), Teamlist())
        val adapter = BelongChartFragmentAdapter(this)
        adapter.fragmentList = fragmentList
        binding.viewPager24.adapter = adapter

        val tabTitle = listOf<String>("팀게시글","갤러리","팀원목록")
        TabLayoutMediator(binding.tabLayout3, binding.viewPager24){tab, position->
            tab.text =tabTitle[position]
        }.attach()

        binding.imageButton.setOnClickListener {
            val intent1 = Intent(this,MadeGroup2Activity::class.java)
            intent1.putExtra("groupId",1)
            startActivityForResult(intent1,99)
        }
     //   binding.imageButton3.setOnClickListener{
          //  val intent1 =Intent(this,MadeGroup3Activity::class.java)
            //intent1.putExtra("url",1)
         //   startActivity(intent1)
       // }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                99->{val image1:String? =data?.getStringExtra("returnUrl")
                  if(image1==null) Toast.makeText(this,"1111",Toast.LENGTH_LONG).show()
                    if(image1!=null){
                   setImageWithGlide1(image1)

                    }}

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
        intent1.putExtra("code",num)
        startActivity(intent1)
    }
}