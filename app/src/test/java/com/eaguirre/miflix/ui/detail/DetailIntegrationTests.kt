package com.eaguirre.miflix.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.miflix.ui.FakeLocalDataSource
import com.eaguirre.miflix.ui.defaultFakeMovies
import com.eaguirre.miflix.ui.initMockedDi
import com.eaguirre.myflix.ui.detail.DetailViewModel
import com.eaguirre.testshared.mockedMovie
import com.eaguirre.usecases.FindMovieById
import com.eaguirre.usecases.ToogleMovieFavorite
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<DetailViewModel.UiModel>

    private lateinit var vm: DetailViewModel
    private lateinit var localDataSource: FakeLocalDataSource

    @Before
    fun setUp() {
        val vmModule = module {
            factory { (id: Int) -> DetailViewModel(id, get(), get(), get()) }
            factory { FindMovieById(get()) }
            factory { ToogleMovieFavorite(get()) }
        }

        initMockedDi(vmModule)
        vm = get { parametersOf(1) }

        localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = defaultFakeMovies
    }

    @Test
    fun `observing LiveData finds the movie`() {
        vm.model.observeForever(observer)

        verify(observer).onChanged(DetailViewModel.UiModel(mockedMovie.copy(1)))
    }

    @Test
    fun `favorite is updated in local data source`() {
        vm.model.observeForever(observer)

        vm.onMovieFavoriteClicked()

        runBlocking {
            assertTrue(localDataSource.findById(1).favorite)
        }
    }
}