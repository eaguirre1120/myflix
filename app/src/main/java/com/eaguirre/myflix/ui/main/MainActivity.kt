package com.eaguirre.myflix.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.model.MovieDbClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        val moviesAdapter = MoviesAdapter(emptyList()) { movie ->
            Toast
                .makeText(this@MainActivity, movie.title, Toast.LENGTH_LONG)
                .show()
        }
        binding.recyclerMovies.adapter = moviesAdapter

        lifecycleScope.launch{
            val apiKey = getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.listPopularMovies(apiKey)
                moviesAdapter.movies = popularMovies.results
                moviesAdapter.notifyDataSetChanged()
            }
        }

        //Trabajado con hilos
        /*thread {
            val apiKey = this.resources.getString(R.string.api_key)
            val popularMovies = MovieDbClient.service.listPopularMovies(apiKey)
            val body = popularMovies.execute().body()
            runOnUiThread {
                if(body != null) {
                    moviesAdapter.movies = body.results
                    moviesAdapter.notifyDataSetChanged()
                }
            }
        }*/

    }
}