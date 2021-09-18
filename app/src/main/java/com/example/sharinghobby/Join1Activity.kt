package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.sharinghobby.databinding.ActivityJoin1Binding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join1.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

        binding.emailcheckButton.setOnClickListener {
            var checkint :Int=0;
            var connector = DBConnector()
            var user_email = binding.userEmail.text.toString();
            CoroutineScope(Dispatchers.Default).launch {

                val data = connector.getData<Account>(user_email!!)
                runBlocking(Dispatchers.Main) {
                    //  Log.e("asdf",data!!.user_phone)
                    if(data==null)
                    checkint=1;
                }
            }
            if(checkint==1){Toast.makeText(this, "중복된 아이디입니다", Toast.LENGTH_SHORT)
                .show(); }
            else{boolemail=true; binding.emailcheckButton.text="OK!!"; Log.d("from123","${data}")}
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