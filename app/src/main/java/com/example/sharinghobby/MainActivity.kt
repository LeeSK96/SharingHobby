package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sharinghobby.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
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
        val intent2 = Intent(this,FindActivity::class.java)
        val goHome = Intent(this,HomeActivity::class.java)
        binding.joinbutton.setOnClickListener {
         startActivity(intent1)
        }
        binding.searchbutton.setOnClickListener {
            startActivity(intent2)
        }
        binding.loginbutton.setOnClickListener {
            var userId=binding.editTextTextPersonName.text.toString()
            var UserIId="";
            var connector = DBConnector()
            var checkId =false;
           /* CoroutineScope(Dispatchers.Default).launch {
                var userData = connector.getData<Account>("")
                if( userData != null) {
                  Log.d("from1",userData?.PW);
                      //checkId=true;
                }
                else    Log.d("from1","${userData?.PW}");
            }*/

            intent.putExtra("uid","$userId")
            startActivity(goHome)}
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