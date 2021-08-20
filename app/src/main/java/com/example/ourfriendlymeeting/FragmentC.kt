package com.example.ourfriendlymeeting

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ourfriendlymeeting.databinding.FragmentCBinding

class FragmentC : Fragment() {
    var categoryActivity: CategoryActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCBinding.inflate(inflater, container, false)
        binding.button8.setOnClickListener {
            categoryActivity?.selectDone("ball")
        }; return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CategoryActivity)categoryActivity=context
    }
}

