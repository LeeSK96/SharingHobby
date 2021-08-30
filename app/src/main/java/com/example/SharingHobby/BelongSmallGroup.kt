package com.example.SharingHobby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ourfriendlymeeting.databinding.ActivityBelongSmallGroupBinding

class BelongSmallGroup : AppCompatActivity() {
    val binding by lazy{ActivityBelongSmallGroupBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}