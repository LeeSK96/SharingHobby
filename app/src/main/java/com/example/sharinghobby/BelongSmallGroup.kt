package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.databinding.ActivityBelongSmallGroupBinding

class BelongSmallGroup : AppCompatActivity() {
    val binding by lazy{ActivityBelongSmallGroupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var Groupinfo = intent.getSerializableExtra("memo");
        Log.d("from1","${Groupinfo.toString()}")
        val comunityId: String? =intent.getStringExtra("uri");
        //if (comunityId != null) {
        //    setImageWithGlide1(comunityId)
       // }
        

        binding.Groupimage.setOnClickListener {
            val intent1 = Intent(this,MadeGroup2Activity::class.java)
            intent1.putExtra("groupId",1)
            startActivityForResult(intent1,99)
        }
        binding.imageButton3.setOnClickListener{
          //  val intent1 =Intent(this,MadeGroup3Activity::class.java)
            //intent1.putExtra("url",1)
         //   startActivity(intent1)
        }
        binding.NameByGroup.text = Groupinfo.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                99->{val image1:String? =data?.getStringExtra("returnUrl")
                  if(image1==null) Toast.makeText(this,"1111",Toast.LENGTH_LONG).show()
                    if(image1!=null){
                   setImageWithGlide1(image1)
                    setImageWithGlide2(image1)
                    setImageWithGlide3(image1)
                    setImageWithGlide4(image1)}}

            }
        }
    }
    fun setImageWithGlide1(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(500,500))
            .into(binding.Groupimage)
    }
    fun setImageWithGlide2(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(500,500))
            .into(binding.imageButton3)
    }
    fun setImageWithGlide3(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(500,500))
            .into(binding.imageButton4)
    }

    fun setImageWithGlide4(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(500,500))
            .into(binding.imageButton5)
    }
}