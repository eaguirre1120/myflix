package com.eaguirre.usecases

import com.eaguirre.data.MoviesRepository
import com.eaguirre.domain.Movie

class GetPopularMovies(
    private val moviesRepository: MoviesRepository
    ) {

    suspend fun invoke(): List<Movie> = moviesRepository.getPopularMovies()
}