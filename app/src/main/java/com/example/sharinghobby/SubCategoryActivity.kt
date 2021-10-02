package com.example.sharinghobby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharinghobby.databinding.ActivitySubCategoryBinding

class SubCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}