package com.eaguirre.usecases

import com.eaguirre.data.MoviesRepository
import com.eaguirre.domain.Movie

class ToogleMovieFavorite(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(movie: Movie) :Movie = with(movie){
        copy(favorite = !favorite).also { moviesRepository.update(it) }
    }
}