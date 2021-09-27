package com.example.sharinghobby

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sharinghobby.databinding.ActivityMadeGroup2Binding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.sql.Date
import java.text.SimpleDateFormat

class MadeGroup2Activity : com.example.sharinghobby.BaseActivity() {
    val PERM_STOREAGE = 99
    val PERM_CAMERA = 100
    val REQ_CAMERA = 101
    val REQ_STOREAGE = 102
    val binding by lazy { ActivityMadeGroup2Binding.inflate(layoutInflater) }
    var realUri: Uri? = null
    var notrealuri:String="";
    val storageRef = FirebaseStorage.getInstance().reference
    var count :Int=0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requirePermissions(
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERM_STOREAGE
        )
        val GroupId:String? =intent.getStringExtra("groupId")
        binding.NextButton.setOnClickListener {
           val returnIntent = Intent()
            if(notrealuri!=null) {
                returnIntent.putExtra("returnUrl",notrealuri)
                setResult(RESULT_OK,returnIntent)
                finish()
            }else Toast.makeText(this,"1111",Toast.LENGTH_LONG).show()
        }
        readDatabase();
    }

    //** Firebase Database 조회*/
    private fun readDatabase() {
        db.collection("File")
            .whereEqualTo("communityId", 1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val data: Map<String, Any> = document.data
                        val communityId = data["communityId"]
                        val url = data["imgurl"]
                        Log.e("Firebase", "$communityId,$url")
                    }
                } else {
                    Toast.makeText(this, "re", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERM_STOREAGE -> setViews()
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_STOREAGE -> {
                Toast.makeText(baseContext, "외부저장소 권한을 승인해야 앱사용이 가능합니다", Toast.LENGTH_LONG).show()
                finish()
            }
            PERM_CAMERA -> {
                Toast.makeText(baseContext, "카메라 권한을 승인해야 사용가능 합니다", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setViews() {
        binding.cameraButton.setOnClickListener {
            requirePermissions(arrayOf(android.Manifest.permission.CAMERA), PERM_CAMERA)
        }
        binding.gellaryButton.setOnClickListener {
            openGallery()
        }
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFilename(), "image/jpg")?.let { uri ->
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_STOREAGE)
    }

    fun newFilename(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    fun createImageUri(filename: String, mimeType: String): Uri? {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    fun uploadStorage(uri: Uri) {
        val filename = SimpleDateFormat("yyyyMMddhhmmss").format(java.util.Date()) + ".jpg"

        //업로드할 위치와 파일 명 (폴더가 없을 경우 자동생성)
        val ref = storageRef.child("image/$filename")
        ref.putFile(uri).addOnProgressListener { taskSnapshot ->
            val btf = taskSnapshot.bytesTransferred
            val tbc = taskSnapshot.totalByteCount
            val progress: Double = (100.0 * btf) / tbc

            //진행 상황 표시
            //progressBar.progress =progress.toInt()
        }
            .addOnFailureListener { Log.e("업로드 실패", "images/$filename") }
            .addOnSuccessListener {
                Log.e("업로드 성공", "images/$filename")
                /** 업로드한 파일의 URL 가져오기*/
                ref.downloadUrl.addOnSuccessListener {
                    /** 3. Firebase DataBase 저장 */
                    Log.e("링크 가져오기 성공", "$it")
                    insertDatabase(it.toString())

                }.addOnFailureListener {
                    Log.e("링크 가져오기 실패", it.message.toString())
                }
            }

    }

    /*firebase database저장*/
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private fun insertDatabase(url: String) {
        /**1. 커뮤니티 생성화면) 사진 선택 -> stoage 에 등록 후 url 물고있기
         * 2. 커뮤니티 등록 버튼을 등록해서 실행할 때, Database 에 url,id 같이 insert
         */
        val map: MutableMap<String, Any> = HashMap()
        map["communityId"] = 1;
        map["imgurl"] = url

        notrealuri=url

        db.collection("File")
            .add(map)
            .addOnSuccessListener { documentReference -> //저장된 데이터의 ID을 획득할 수 있습니다.
                //documentReference.id
                Log.e("firebase DB Insert 완료", "DocumentSnapshot added with ID: $documentReference")

                setImageWithGlide1(url)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase Db Insert 실패", e.message.toString())
            }
    }

    fun loadBitmap(photoUri: Uri): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (Build.VERSION.SDK_INT > 27) {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(this.contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_CAMERA -> {
                    realUri?.let { uri ->
                        val bitmap = loadBitmap(uri)
                        // binding.imageView6.setImageBitmap(bitmap)
                        uploadStorage(uri)
                        realUri = null
                    }
                }
                REQ_STOREAGE -> {
                    data?.data?.let { uri ->
                        // binding.imageView6.setImageURI(uri)
                        uploadStorage(uri)
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
            .into(binding.imageView6)
           binding.NextButton.visibility= View.VISIBLE
           binding.NextButton.text="설정완료!"
    }
    fun setImageWithGlide2(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(200,500))
            .into(binding.imageView6)
    }
    fun setImageWithGlide3(url:String){
        Glide.with(binding.root.context)
            .load(url)
            .thumbnail(0.5f)
            .centerCrop()
            .apply(RequestOptions().override(200,500))
            .into(binding.imageView6)
    }
}