package com.example.sharinghobby

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.sharinghobby.databinding.ActivityMadeGroupBinding

class MadeGroupActivity : AppCompatActivity() {
    val binding by lazy { ActivityMadeGroupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val uid = intent.getIntExtra("from1",0)
        val locate =intent.getIntExtra("location",0)
        var data1 = listOf<String>("선택하세요","ball","낚시","골프","음악밴드")
        var data2 = listOf<String>("선택하세요","pc게임","보드게임","당구","공예")
        var str="s"
        val Groupname=binding.groupname.text
        val Limit = binding.limited.text
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioButton5->{  var adapter =ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data1)
                    binding.spinner2.adapter =adapter
                binding.spinner2.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                    str = data1[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }}
                R.id.radioButton6->{  var adapter =ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data2)
                    binding.spinner2.adapter =adapter
                binding.spinner2.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        str=data2[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                }}
            }
        }
        binding.button10.setOnClickListener {
            val intent1 = Intent(this,MadeGroup2Activity::class.java)
            intent1.putExtra("from1",uid)
            intent1.putExtra("location",locate)
            intent1.putExtra("groupname",Groupname)
            intent1.putExtra("limit",Limit)
            intent1.putExtra("str",str)
            startActivity(intent1)
        }

    }
}