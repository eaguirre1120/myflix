package com.eaguirre.myflix.ui.main

import com.eaguirre.myflix.model.Movie
import com.eaguirre.myflix.model.MoviesRepository
import com.eaguirre.myflix.ui.common.Scope
import kotlinx.coroutines.launch

class MainPresenter(private val moviesRepository: MoviesRepository) : Scope by Scope.Impl() {

    interface View{
        fun showProgress()
        fun hideProgress()
        fun updateData(movies: List<Movie>)
        fun navigateTo(movie: Movie)
    }

    private var view: View? = null

    fun onCreate(view: View) {
        initScope()
        this.view = view
        launch {
            view.showProgress()
            view.updateData(moviesRepository.findPopularMovies().results)
            view.hideProgress()
        }
    }

    fun onDestroy() {
        cancelScope()
        this.view = null
    }

    fun onMovieClicked(movie: Movie) {
        view?.navigateTo(movie)
    }
}