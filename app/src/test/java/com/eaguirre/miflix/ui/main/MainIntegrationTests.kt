package com.eaguirre.miflix.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.miflix.ui.FakeLocalDataSource
import com.eaguirre.miflix.ui.defaultFakeMovies
import com.eaguirre.miflix.ui.initMockedDi
import com.eaguirre.myflix.ui.main.MainViewModel
import com.eaguirre.testshared.mockedMovie
import com.eaguirre.usecases.GetPopularMovies
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainIntegrationTests: AutoCloseKoinTest() {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<MainViewModel.UiModel>

    private lateinit var vm: MainViewModel

    @Before
    fun setUp(){
        val vmModule = module {
            factory { MainViewModel(get(), get()) }
            factory { GetPopularMovies(get()) }
        }

        initMockedDi(vmModule)
        vm = get()
    }

    @Test
    fun `data is loaded from server when local source is empty`() {
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MainViewModel.UiModel.Content(defaultFakeMovies))
    }

    @Test
    fun `data is loaded from local source when available`() {
        val fakeLocalMovies = listOf(mockedMovie.copy(10), mockedMovie.copy(11))
        val localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = fakeLocalMovies
        vm.model.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MainViewModel.UiModel.Content(fakeLocalMovies))
    }

}