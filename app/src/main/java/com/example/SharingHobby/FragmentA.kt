package com.example.SharingHobby

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ourfriendlymeeting.databinding.FragmentABinding

class FragmentA : Fragment() {
    var findActivity:FindActivity? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val binding =FragmentABinding.inflate(inflater,container,false)
        var str:String?
        //쓰레드나 코루틴 사용해서 조회서비스 백그라운드에서 돌려야합니다
        binding.button2.setOnClickListener {
            findActivity?.showinfoId("dlwodb95",0);

        };   return binding.root
    }




    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FindActivity) findActivity = context
    }

}