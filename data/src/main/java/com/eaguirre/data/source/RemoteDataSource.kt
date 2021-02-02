package com.eaguirre.data.source

import com.eaguirre.domain.Movie

interface RemoteDataSource {
    suspend fun getPopularMovies(apiKe: String, region: String): List<Movie>
}