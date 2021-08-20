package com.example.ourfriendlymeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ourfriendlymeeting.databinding.ActivityJoinBinding
import com.example.ourfriendlymeeting.databinding.ActivityMainBinding

class JoinActivity : AppCompatActivity() {
    val binding by lazy{ ActivityJoinBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val intent =Intent(this,Join1Activity::class.java)
        setFragment()
        var ch = false
        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) ch=true
            else ch=false
        }
        binding.button.setOnClickListener {
            if(ch)startActivityForResult(intent,99)
            else Toast.makeText(baseContext,"약관동의가 필요합니다.",Toast.LENGTH_LONG).show()
        }
    }
    fun setFragment(){
        val joinFragment :JoinFragment = JoinFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frameLayout,joinFragment)
        transaction.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK){
            when(requestCode){
                99->{finish()}
            }
        }
    }
}