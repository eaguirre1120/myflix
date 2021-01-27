package com.eaguirre.myflix.model.server

import android.app.Application
import com.eaguirre.myflix.MoviesApp
import com.eaguirre.myflix.R
import com.eaguirre.myflix.model.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.eaguirre.myflix.model.database.Movie as DbMovie
import com.eaguirre.myflix.model.server.Movie as ServerMovie

class MoviesRepository(application: MoviesApp) {

    private val apiKey = application.getString(R.string.api_key)
    private val regionRepository = RegionRepository(application)
    private val db = application.db

    suspend fun findPopularMovies() : List<DbMovie> = withContext(Dispatchers.IO){
        with(db.movieDao()){
            if(countMovie() <= 0){
                val movies = MovieDb.service
                    .listPopularMovies(apiKey,regionRepository.findLastRegion())
                    .results
                db.movieDao().insertMovies(movies.map(ServerMovie::convertToDbMovie) )
            }
            getAll()
        }
    }

    suspend fun findById(id: Int): DbMovie = withContext(Dispatchers.IO){
        db.movieDao().findById(id)
    }

    suspend fun update(movie: DbMovie) = withContext(Dispatchers.IO){
        db.movieDao().updateMovie(movie)
    }
}
private fun ServerMovie.convertToDbMovie() = DbMovie(
    0,
    title,
    overview,
    release_date,
    poster_path,
    backdrop_path?:poster_path,
    original_language,
    original_title,
    popularity,
    vote_average,
    false
)