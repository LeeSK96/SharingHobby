package com.example.sharinghobby

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.sharinghobby.databinding.ActivityCenterBinding

class CenterActivity : com.example.sharinghobby.BaseActivity() {
    val PERM_FINE_LOCATION=100;
    var location_state=false;
    val binding by lazy { ActivityCenterBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var lst=intent.getIntExtra("location",0)
        val uid = intent.getIntExtra("from1",0)
        val intent1 = Intent(this,MBselectedActivity::class.java)
        val intent2 = Intent(this,SearchAndMake::class.java)
       if(lst==0)requirePermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),PERM_FINE_LOCATION)
        binding.clubbutton.setOnClickListener {
            intent1.putExtra("from1",uid)
            startActivity(intent1)
        }
        binding.button5.setOnClickListener {
            intent2.putExtra("from1",uid)
            startActivityForResult(intent2,99)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                99->{
                    val Mycategory =data?.getStringExtra("returnS")
                    Toast.makeText(this,"$Mycategory 에대한 위치검색서비스 시작 ",Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
    override fun permissionGranted(requestCode: Int) {
        when(requestCode){
           // PERM_STORAGE->{state2=1; setViews()}

        }
    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_FINE_LOCATION->{
                Toast.makeText(baseContext,"위치 허용 권한을 승인해야 서비스의 이용이 가능합니다.", Toast.LENGTH_LONG).show()
            }

        }
    }
}