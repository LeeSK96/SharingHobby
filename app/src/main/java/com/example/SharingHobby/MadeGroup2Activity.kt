package com.example.SharingHobby

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaActionSound
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.ourfriendlymeeting.databinding.ActivityMadeGroup2Binding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.jar.Manifest

class MadeGroup2Activity : com.example.SharingHobby.BaseActivity() {
    val PERM_STOREAGE =99
    val PERM_CAMERA =100
    val REQ_CAMERA =101
    val REQ_STOREAGE=102
    val binding by lazy { ActivityMadeGroup2Binding.inflate(layoutInflater) }
    var realUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requirePermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),PERM_STOREAGE)
        binding.NextButton.setOnClickListener {
            val intent = Intent(this,MadeGroup3Activity::class.java)
            startActivity(intent)
        }
    }
    override fun permissionGranted(requestCode: Int) {
      when(requestCode){
          PERM_STOREAGE ->setViews()
          PERM_CAMERA->openCamera()
      }
    }

    override fun permissionDenied(requestCode: Int) {
      when(requestCode){
          PERM_STOREAGE->{
              Toast.makeText(baseContext,"외부저장소 권한을 승인해야 앱사용이 가능합니다",Toast.LENGTH_LONG).show()
              finish()
          }
          PERM_CAMERA->{
              Toast.makeText(baseContext,"카메라 권한을 승인해야 사용가능 합니다",Toast.LENGTH_LONG).show()
          }
      }
    }
  fun setViews(){
      binding.cameraButton.setOnClickListener {
          requirePermissions(arrayOf(android.Manifest.permission.CAMERA),PERM_CAMERA)
      }
      binding.gellaryButton.setOnClickListener {
          openGallery()
      }
  }
    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       createImageUri(newFilename(),"image/jpg")?.let{uri ->
           realUri =uri
           intent.putExtra(MediaStore.EXTRA_OUTPUT,realUri)
           startActivityForResult(intent,REQ_CAMERA)
       }
    }
    fun openGallery(){
        val intent =Intent(Intent.ACTION_PICK)
        intent.type =MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent,REQ_STOREAGE)
    }
    fun newFilename():String{
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val filename =sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }
    fun createImageUri(filename:String ,mimeType: String):Uri?{
        var values =ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }
    fun loadBitmap(photoUri:Uri): Bitmap?{
        var image: Bitmap? =null
        try{
            image =if(Build.VERSION.SDK_INT>27){
                val source:ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver,photoUri)
                ImageDecoder.decodeBitmap(source)
            }else{
                MediaStore.Images.Media.getBitmap(this.contentResolver,photoUri)
            }

        }catch (e:IOException){
            e.printStackTrace()
        }
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                REQ_CAMERA->{
                    realUri?.let {uri->
                        val bitmap =loadBitmap(uri)
                        binding.imageView6.setImageBitmap(bitmap)
                        realUri =null
                    } }
                REQ_STOREAGE->{
                data?.data?.let { uri->
                    binding.imageView6.setImageURI(uri)
                }
            }
            }
        }
    }
}