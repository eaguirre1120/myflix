package com.eaguirre.usecases

import com.eaguirre.data.MoviesRepository
import com.eaguirre.domain.Movie

class FindMovieById(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(id: Int): Movie = moviesRepository.findById(id)
}