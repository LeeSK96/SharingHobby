package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.sharinghobby.databinding.ActivityJoin1Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Join1Activity : AppCompatActivity() {
    val binding by lazy { ActivityJoin1Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var userId =""
        var userPassword =""
        var nickname =""
        var pressedId:String =""
        var usertelNumber:String="00000000000";
        var boolId =false;
        var boolPw =false;
        var boolNn =false;
        var boolemail =false;
        var boolgender =false;
        var gender=""
        var data = listOf("성별선택","남자","여자")
        var adapter =ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data)
        binding.spinner.adapter=adapter
        binding.spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                gender=data[position]
                boolgender=true;
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.checkId.setOnClickListener {
            //if (데이터베이스에서 가져올것 == binding.accountId.text.tostring()){토스트 메세지 출력 이미 있는 아이디 }
            //else checkID=true
            if(userId==binding.accountId.text.toString()) {Toast.makeText(this, "중복된 아이디입니다", Toast.LENGTH_SHORT)
                .show(); binding.checkId.text="중복확인"}
            else{boolId=true; binding.checkId.text="OK!!"; userId=binding.accountId.text.toString() }
        }
        binding.joinus.setOnClickListener {
            //  if(boolId&&boolPw&&boolNn&&boolemail&&boolgender){}
            val auth = Firebase.auth
            auth.createUserWithEmailAndPassword(binding.userEmail.text.toString(),binding.password.text.toString())
                .addOnSuccessListener {
                    var OurDB = DBConnector();
                    var accountdata = Account(userId,binding.password.text.toString(),binding.userEmail.text.toString(),binding.telnumber.text.toString(),"1",binding.nickname.text.toString());
                    OurDB.setAccountData(accountdata,auth.currentUser!!.uid);
                    Toast.makeText(this, "회원가입완료!", Toast.LENGTH_SHORT).show()
                }

            val returnIntent = Intent()
            returnIntent.putExtra("from1","1")
            setResult(RESULT_OK,returnIntent)
            finish()
        }
    }
}