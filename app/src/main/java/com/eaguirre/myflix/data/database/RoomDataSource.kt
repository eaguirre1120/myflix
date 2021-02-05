package com.eaguirre.myflix.data.database

import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.domain.Movie
import com.eaguirre.myflix.data.toDomainMovie
import com.eaguirre.myflix.data.toRoomMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: MovieDatabase): LocalDataSource {

    private val movieDao = db.movieDao()

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO){ movieDao.countMovie() <= 0 }

    override suspend fun saveMovies(movies: List<Movie>) {
        withContext(Dispatchers.IO) {
            movieDao.insertMovies(movies.map{it.toRoomMovie()})
        }
    }

    override suspend fun getPopularMovies(): List<Movie> = withContext(Dispatchers.IO){
        movieDao.getAll().map { it.toDomainMovie() }
    }

    override suspend fun findById(id: Int): Movie = withContext(Dispatchers.IO){
        movieDao.findById(id).toDomainMovie()
    }

    override suspend fun update(movie: Movie){
        withContext(Dispatchers.IO){
            movieDao.updateMovie(movie.toRoomMovie())
        }
    }
}