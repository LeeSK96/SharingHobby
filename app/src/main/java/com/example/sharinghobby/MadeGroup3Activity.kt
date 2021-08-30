package com.example.sharinghobby

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sharinghobby.databinding.ActivityMadeGroup3Binding

class MadeGroup3Activity : AppCompatActivity() {
    val binding by lazy { ActivityMadeGroup3Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}