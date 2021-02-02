package com.eaguirre.myflix.model.server

import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.domain.Movie
import com.eaguirre.myflix.model.toDomainMovie

class TheMovieDbDataSource: RemoteDataSource {
    override suspend fun getPopularMovies(apiKey: String, region: String): List<Movie> =
        TheMovieDb.service
                .listPopularMovies(apiKey, region)
                .results
                .map { it.toDomainMovie() }
}