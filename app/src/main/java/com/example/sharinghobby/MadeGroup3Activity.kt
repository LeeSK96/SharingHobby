package com.example.sharinghobby

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.databinding.ActivityMadeGroup2Binding
import com.example.sharinghobby.databinding.ActivityMadeGroup3Binding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MadeGroup3Activity : AppCompatActivity() {
    val PERM_STOREAGE = 99
    val PERM_CAMERA = 100
    val REQ_CAMERA = 101
    val REQ_STOREAGE = 102
    val binding by lazy { ActivityMadeGroup3Binding.inflate(layoutInflater) }
    var realUri: Uri? = null
    val storageRef = FirebaseStorage.getInstance().reference
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val comunityId: String? =intent.getStringExtra("uri");
        if (comunityId != null) {
            setImageWithGlide1(comunityId)
        }
        //readDatabase(comunityId)
    }
    private fun readDatabase(GroupId:Int) {
        db.collection("File")
            .whereEqualTo("communityId", GroupId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val data: Map<String, Any> = document.data
                        val communityId = data["communityId"]
                        val url = data["imgurl"]
                        Log.e("Firebase", "$communityId,$url")
                        setImageWithGlide1(url.toString())
                    }
                } else {
                    Toast.makeText(this, "re", Toast.LENGTH_LONG).show()
                }
            }
    }
    fun setImageWithGlide1(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(200,500))
            .into(binding.imageView8)
    }
    fun setImageWithGlide2(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(200,500))
            .into(binding.imageView9)
    }
    fun setImageWithGlide3(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(200,500))
            .into(binding.imageView10)
    }
}