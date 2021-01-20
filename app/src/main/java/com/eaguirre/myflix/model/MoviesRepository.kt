package com.eaguirre.myflix.model

import android.app.Activity
import android.app.Application
import com.eaguirre.myflix.R

class MoviesRepository(application: Application) {

    private val apiKey = application.getString(R.string.api_key)
    private val regionRepository = RegionRepository(application)

    suspend fun findPopularMovies() =
        MovieDbClient
            .service
            .listPopularMovies(apiKey, regionRepository.findLastRegion())

}