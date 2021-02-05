package com.eaguirre.myflix.data.server

import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.domain.Movie
import com.eaguirre.myflix.data.toDomainMovie

class TheMovieDbDataSource: RemoteDataSource {
    override suspend fun getPopularMovies(apiKey: String, region: String): List<Movie> =
        TheMovieDb.service
                .listPopularMovies(apiKey, region)
                .results
                .map { it.toDomainMovie() }
}