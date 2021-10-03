package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sharinghobby.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class loginActivity : AppCompatActivity() {
    val binding by lazy{ ActivityLoginBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        thread(start=true){
        Thread.sleep(2000)
            runOnUiThread{
        showProgress(false)
            showButtons(true)
            }
        }

        val intent1 = Intent(this,JoinActivity::class.java)
        val intent2 = Intent(this,FindAccountActivity::class.java)
        val goHome = Intent(this,HomeActivity::class.java)
        binding.joinbutton.setOnClickListener {
         startActivity(intent1)
        }
        binding.searchbutton.setOnClickListener {
            startActivity(intent2)
        }
        binding.loginbutton.setOnClickListener {
            //startActivity(goHome)
            var userId=binding.editTextTextPersonName.text.toString()
            var userPw = binding.editTextTextPersonName2.text.toString()
            var connector = DBConnector()
            var check=0;
            var auth = Firebase.auth
            var warning1:String="";
            if(userId==""||userPw=="")Toast.makeText(this,"ID또는 PW를 입력하지 않았습니다.",Toast.LENGTH_LONG).show()
            else{
            auth.signInWithEmailAndPassword(userId,userPw)
                .addOnSuccessListener {
                    CoroutineScope(Dispatchers.Default).launch {
                        val uid = auth.uid
                        val data = connector.getData<Account>(uid!!)
                        warning1=data?.user_email.toString()
                        if(data?.id!=null){
                        runBlocking(Dispatchers.Main) {
                          //  Log.e("asdf",data!!.user_phone)
                            Log.e("uid", uid)
                            goHome.putExtra("uid",uid)
                            check =1;
                            startActivity(goHome)
                        }
                        }

                    }
                }}
            if(check==0)Toast.makeText(this@loginActivity,"존재하지 않는 Id입니다.",Toast.LENGTH_LONG).show()

           /*CoroutineScope(Dispatchers.Default).launch {
                var userData = connector.getData<Account>("")
                if( userData != null) {
                  Log.d("from1",userData?.PW);
                      //checkId=true;
                }
                else    Log.d("from1","${userData?.PW}");
            }*/

        }
            //else Toast.makeText(this, "아이디 또는 패스워드가 틀렸습니다", Toast.LENGTH_SHORT).show()

    }
    fun showProgress(show:Boolean){
        if(show)binding.imageView5.visibility = View.VISIBLE
        else binding.imageView5.visibility=View.GONE
    }
    fun showButtons(show: Boolean){
        binding.loginbutton.visibility = View.VISIBLE
        binding.joinbutton.visibility = View.VISIBLE
        binding.searchbutton.visibility = View.VISIBLE
        binding.editTextTextPersonName.visibility = View.VISIBLE
        binding.editTextTextPersonName2.visibility = View.VISIBLE
    }
}