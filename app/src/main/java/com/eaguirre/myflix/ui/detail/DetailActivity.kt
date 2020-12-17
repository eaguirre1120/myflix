package com.eaguirre.myflix.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}