package com.eaguirre.miflix.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eaguirre.myflix.ui.detail.DetailViewModel
import com.eaguirre.myflix.ui.detail.DetailViewModel.*
import com.eaguirre.testshared.mockedMovie
import com.eaguirre.usecases.FindMovieById
import com.eaguirre.usecases.ToogleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var findMovieById: FindMovieById

    @Mock
    lateinit var toogleMovieFavorite: ToogleMovieFavorite

    @Mock
    lateinit var observer: Observer<UiModel>

    private lateinit var vm: DetailViewModel

    @Before
    fun setUp(){
        vm = DetailViewModel(1, findMovieById, toogleMovieFavorite, Dispatchers.Unconfined)
    }

    @Test
    fun `observing LiveData finds the movie`(){
        runBlocking {
            val movie = mockedMovie.copy(1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)

            vm.model.observeForever(observer)

            verify(observer).onChanged(UiModel(movie))
        }
    }

    @Test
    fun `when favorite clicked, the toogleMovieFavorite use case is invoke`(){
        runBlocking {
            val movie = mockedMovie.copy(1)
            whenever(findMovieById.invoke(1)).thenReturn(movie)
            whenever(toogleMovieFavorite.invoke(movie)).thenReturn(movie.copy(favorite = !movie.favorite))

            vm.model.observeForever(observer)

            verify(toogleMovieFavorite).invoke(movie)
        }
    }
}