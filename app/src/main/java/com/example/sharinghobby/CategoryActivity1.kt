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
            startActivity (
                Intent(this, HomeActivity::class.java).apply {
                    putExtra("categoryNumber", "1")
                }
            )
        }

        binding.cultureButton.setOnClickListener {
            startActivity (
                Intent(this, HomeActivity::class.java).apply {
                    putExtra("categoryNumber", "2")
                }
            )
        }

        binding.threeButton.setOnClickListener {
            startActivity (
                Intent(this, HomeActivity::class.java).apply {
                    putExtra("categoryNumber", "3")
                }
            )
        }

        binding.fourButton.setOnClickListener {
            startActivity (
                Intent(this, HomeActivity::class.java).apply {
                    putExtra("categoryNumber", "4")
                }
            )
        }

        binding.fiveButton.setOnClickListener {
            startActivity (
                Intent(this, HomeActivity::class.java).apply {
                    putExtra("categoryNumber", "5")
                }
            )
        }
    }
}