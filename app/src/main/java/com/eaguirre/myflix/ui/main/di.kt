package com.eaguirre.myflix.ui.main

import com.eaguirre.data.MoviesRepository
import com.eaguirre.usecases.GetPopularMovies
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun mainViewModelProvider(getPopularMovies: GetPopularMovies) = MainViewModel(getPopularMovies)

    @Provides
    fun getPopularMovies(moviesRepository: MoviesRepository) = GetPopularMovies(moviesRepository)
}

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent{
    val mainViewModel: MainViewModel
}