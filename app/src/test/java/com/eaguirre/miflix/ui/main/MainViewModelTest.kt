package com.eaguirre.miflix.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eaguirre.myflix.ui.main.MainViewModel
import com.eaguirre.myflix.ui.main.MainViewModel.UiModel
import com.eaguirre.testshared.mockedMovie
import com.eaguirre.usecases.GetPopularMovies
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
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getPopularMovies: GetPopularMovies

    @Mock
    lateinit var observer: Observer<UiModel>

    private lateinit var vm: MainViewModel

    @Before
    fun setUp(){
        vm = MainViewModel(getPopularMovies,Dispatchers.Unconfined)
    }

    @Test
    fun `observing Livedata launches location permission request`(){
        vm.model.observeForever(observer)
        verify(observer).onChanged(UiModel.RequestLocationPermission)
    }

    @Test
    fun `after requesting the permission, loading is shown`(){
        runBlocking {
            val movies = listOf(mockedMovie.copy(6))
            whenever(getPopularMovies.invoke()).thenReturn(movies)
            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(UiModel.Loading)
        }
    }

    @Test
    fun `after requesting the permission, getPopularMovies is calls`(){
        runBlocking {
            val movies = listOf(mockedMovie.copy(8))
            whenever(getPopularMovies.invoke()).thenReturn(movies)

            vm.model.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(UiModel.Content(movies))
        }
    }
}