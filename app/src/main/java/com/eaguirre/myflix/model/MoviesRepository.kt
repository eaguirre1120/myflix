package com.eaguirre.myflix.model

import android.app.Activity
import com.eaguirre.myflix.R

class MoviesRepository(activity: Activity) {

    private val apiKey = activity.getString(R.string.api_key)
    private val regionRepository = RegionRepository(activity)

    suspend fun findPopularMovies() =
        MovieDbClient
            .service
            .listPopularMovies(apiKey, regionRepository.findLastRegion())

}