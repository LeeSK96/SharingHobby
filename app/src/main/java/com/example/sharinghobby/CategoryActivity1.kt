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

        binding.searchToolbar.toolbarTitle.setText("취미 카테고리를 선택해주세요.")
        binding.sportButton.setOnClickListener {
            val intent = Intent().putExtra("categoryNumber", "스포츠")
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.cultureButton.setOnClickListener {
            val intent = Intent().putExtra("categoryNumber", "문화")
            setResult(RESULT_OK, intent)
            finish()
        }

        binding.gameButton.setOnClickListener {

        }

        binding.makeButton.setOnClickListener {

        }

        binding.studyButton.setOnClickListener {

        }

        binding.etcButton.setOnClickListener {

        }
    }
}