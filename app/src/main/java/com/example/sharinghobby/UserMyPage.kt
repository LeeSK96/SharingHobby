package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.databinding.ActivityLoginBinding
import com.example.sharinghobby.databinding.ActivityUserMyPageBinding

class UserMyPage : AppCompatActivity() {
    val binding by lazy{ ActivityUserMyPageBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.imageButton7.setOnClickListener {
            val userIndex = intent.getStringExtra("uid")!!
            val intent1 = Intent(this,SetUserPhoto::class.java)
            intent1.putExtra("uid",userIndex)
            startActivityForResult(intent1,99)
        }

    } override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                99->{val image1:String? =data?.getStringExtra("returnUrl")
                    if(image1==null) Toast.makeText(this,"1111", Toast.LENGTH_LONG).show()
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
            .into(binding.imageButton7)
    }
}