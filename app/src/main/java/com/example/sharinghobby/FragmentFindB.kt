package com.example.sharinghobby

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharinghobby.databinding.FragmentFindBBinding

class FragmentFindB : Fragment() {
  var findActivity : FindAccountActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var checkid=false
        var checkname=false
        var checkphone=false
        val binding = FragmentFindBBinding.inflate(inflater,container,false)
        binding.signbutton.setOnClickListener {
            //휴대폰인증처리 연결해야함
            //if(휴대폰연결처리 인증되면)checkphone =true
        }
        binding.finishbutton.setOnClickListener {
            //id를 db조회로 이름과 폰번호 전부 끌어와서 검사후 모든게 일치하면 checkid,checkname true
            findActivity?.showinfoId("1q2w3e4r",1)

        }; return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FindAccountActivity) findActivity = context
    }
}