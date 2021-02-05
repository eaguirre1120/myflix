package com.eaguirre.myflix.ui.detail

import com.eaguirre.data.MoviesRepository
import com.eaguirre.usecases.FindMovieById
import com.eaguirre.usecases.ToogleMovieFavorite
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class DetailActivityModule(private val movieId: Int) {
    @Provides
    fun detailViewModelProvider(
            findMovieById: FindMovieById,
            toogleMovieFavorite: ToogleMovieFavorite
    ): DetailViewModel{
        return DetailViewModel(movieId, findMovieById, toogleMovieFavorite)
    }

    @Provides
    fun findMovieByIdProvider(moviesRepository: MoviesRepository) = FindMovieById(moviesRepository)

    @Provides
    fun toogleMovieFavoriteProvider(moviesRepository: MoviesRepository)= ToogleMovieFavorite(moviesRepository)
}

@Subcomponent(modules = [DetailActivityModule::class])
interface DetailActivityComponent{
    val detailViewModel: DetailViewModel
}