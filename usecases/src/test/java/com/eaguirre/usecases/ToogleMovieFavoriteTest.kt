package com.eaguirre.usecases

import com.eaguirre.data.MoviesRepository
import com.eaguirre.testshared.mockedMovie
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ToogleMovieFavoriteTest {
    @Mock
    lateinit var moviesRepository: MoviesRepository

    lateinit var toogleMovieFavorite: ToogleMovieFavorite

    @Before
    fun setUp(){
        toogleMovieFavorite = ToogleMovieFavorite(moviesRepository)
    }

    @Test
    fun `favorite movie becomes unfavorite`(){
        runBlocking {
            val movie = mockedMovie.copy(favorite = true)

            val result = toogleMovieFavorite.invoke(movie)

            verify(moviesRepository).update(result)
        }
    }

    @Test
    fun `unfavorite movie becomes favorite`(){
        runBlocking {
            val movie = mockedMovie.copy(favorite= false)

            val result = toogleMovieFavorite.invoke(movie)

            verify(moviesRepository).update(result)
        }
    }
}