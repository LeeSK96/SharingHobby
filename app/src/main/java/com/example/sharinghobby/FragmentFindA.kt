package com.example.sharinghobby

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sharinghobby.databinding.FragmentFindABinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FragmentFindA : Fragment() {
    var findActivity:FindAccountActivity? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val binding = FragmentFindABinding.inflate(inflater,container,false)
        var str:String?
        //쓰레드나 코루틴 사용해서 조회서비스 백그라운드에서 돌려야합니다
        binding.button2.setOnClickListener {
            findActivity?.showinfoId("dlwodb95",0);


        };   return binding.root
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FindAccountActivity) findActivity = context
    }

}