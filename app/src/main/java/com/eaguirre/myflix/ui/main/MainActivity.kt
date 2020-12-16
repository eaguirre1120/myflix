package com.eaguirre.myflix.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.model.Movie

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        binding.recyclerMovies.adapter = MoviesAdapter(
                listOf(
                        Movie("Title 1", "https://loremflickr.com/320/240?lock=1"),
                        Movie("Title 2", "https://loremflickr.com/320/240?lock=2"),
                        Movie("Title 3", "https://loremflickr.com/320/240?lock=3"),
                        Movie("Title 4", "https://loremflickr.com/320/240?lock=4"),
                        Movie("Title 5", "https://loremflickr.com/320/240?lock=5"),
                        Movie("Title 6", "https://loremflickr.com/320/240?lock=6")
                )
        )

    }
}