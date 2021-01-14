package com.eaguirre.myflix.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.model.Movie
import com.eaguirre.myflix.model.MovieDbClient
import com.eaguirre.myflix.model.MoviesRepository
import com.eaguirre.myflix.ui.detail.DetailActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class MainActivity : AppCompatActivity() {

    private val moviesAdapter = MoviesAdapter(emptyList()) { movie ->
        navagateTo(movie)
    }

    private val moviesRepository: MoviesRepository by lazy { MoviesRepository(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        binding.recyclerMovies.adapter = moviesAdapter
        lifecycleScope.launch {
            moviesAdapter.movies = moviesRepository.findPopularMovies().results
            moviesAdapter.notifyDataSetChanged()
        }

    }

    private fun navagateTo(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}
