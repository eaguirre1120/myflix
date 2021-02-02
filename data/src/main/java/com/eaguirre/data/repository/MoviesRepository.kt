package com.eaguirre.data

import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.domain.Movie

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val regionRepository: RegionRepository,
    private val apiKey: String
    ) {

    suspend  fun getPopularMovies(): List<Movie>{
        if (localDataSource.isEmpty()){
            val movies = remoteDataSource.getPopularMovies(apiKey, regionRepository.findLastRegion())
            localDataSource.saveMovies(movies)
        }
        return localDataSource.getPopularMovies()
    }
    suspend fun findById(id: Int): Movie = localDataSource.findById(id)

    suspend fun update(movie: Movie) = localDataSource.update(movie)
}