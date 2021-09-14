package com.example.sharinghobby

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharinghobby.databinding.ActivityCategory1Binding
import com.example.sharinghobby.databinding.ActivityCategoryBinding

class CategoryActivity1: AppCompatActivity() {

    private lateinit var binding : ActivityCategory1Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategory1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sportButton.setOnClickListener {
            val intent = Intent().putExtra("categoryNumber", "1")
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.cultureButton.setOnClickListener {

        }

        binding.threeButton.setOnClickListener {

        }

        binding.fourButton.setOnClickListener {

        }

        binding.fiveButton.setOnClickListener {

        }

        binding.sixButton.setOnClickListener {

        }
    }
}