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
import com.eaguirre.myflix.ui.detail.DetailActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class MainActivity : AppCompatActivity() {

    companion object{
        private const val DEFAULT_REGION = "US"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val moviesAdapter = MoviesAdapter(emptyList()) { movie ->
        navagateTo(movie)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
            doRequestPopularMovies(isGranted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.recyclerMovies.adapter = moviesAdapter

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)

    }

    private fun doRequestPopularMovies(isLocationGrated: Boolean) {
        lifecycleScope.launch{
            val apiKey = getString(R.string.api_key)
            val region = getRegion(isLocationGrated)
            Log.d("MainActivity", "Region: $region")
            val popularMovies = MovieDbClient.service.listPopularMovies(apiKey, region)
            moviesAdapter.movies = popularMovies.results
            moviesAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getRegion(isLocationsGranted: Boolean): String = suspendCancellableCoroutine {
        continuation ->
        if (isLocationsGranted) {
            fusedLocationClient.lastLocation.addOnCompleteListener {
                continuation.resume(getRegionFromLocation(it.result))
            }
        } else {
            continuation.resume(DEFAULT_REGION)
        }
    }

    private fun getRegionFromLocation(location: Location?): String {
        if(location == null){
            return DEFAULT_REGION
        }
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
        )

        return addresses.firstOrNull()?.countryCode ?: DEFAULT_REGION
    }

    private fun navagateTo(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}
