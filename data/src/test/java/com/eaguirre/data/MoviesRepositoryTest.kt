package com.eaguirre.data

import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesRepositoryTest {
    @Mock
    lateinit var localDataSource: LocalDataSource

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    @Mock
    lateinit var regionRepository: RegionRepository

    lateinit var moviesRepository: MoviesRepository

    private val apiKey = "AP77878SFJJFK"

    @Before
    fun setUp(){
        moviesRepository = MoviesRepository(localDataSource, remoteDataSource, regionRepository, apiKey)
    }

    @Test
    fun `getPopularMovies gets from local data source first`(){
        runBlocking {
            val localMovies = listOf(mockedMovie.copy(1))

            whenever(localDataSource.isEmpty()).thenReturn(false)
            whenever(localDataSource.getPopularMovies()).thenReturn(localMovies)

            val result = moviesRepository.getPopularMovies()

            Assert.assertEquals(localMovies, result)
        }
    }

    @Test
    fun `getPopularMovies saves remote data to local`(){
        runBlocking {
            val remoteMovies = listOf(mockedMovie.copy(2))
            whenever(localDataSource.isEmpty()).thenReturn(true)
            whenever(remoteDataSource.getPopularMovies(any(), any())).thenReturn(remoteMovies)
            whenever(regionRepository.findLastRegion()).thenReturn("US")

            moviesRepository.getPopularMovies()

            verify(localDataSource).saveMovies(remoteMovies)
        }
    }

    @Test
    fun `findById calls local data source`(){
        runBlocking {
            val movie = mockedMovie.copy(4)
            whenever(localDataSource.findById(4)).thenReturn(movie)

            val result = moviesRepository.findById(4)

            Assert.assertEquals(movie, result)

        }
    }

    @Test
    fun `update updates local data source`(){
        runBlocking {
            val movie = mockedMovie.copy(8)

            moviesRepository.update(movie)

            verify(localDataSource).update(movie)
        }
    }
}