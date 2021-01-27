package com.eaguirre.myflix.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eaguirre.myflix.model.database.Movie
import com.eaguirre.myflix.model.server.MoviesRepository
import com.eaguirre.myflix.ui.common.Event
import com.eaguirre.myflix.ui.common.Scope
import com.eaguirre.myflix.ui.common.ScopedViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val  moviesRepository: MoviesRepository) : ScopedViewModel(){

    sealed class UiModel{
        object Loading : UiModel()
        class Content(val movies: List<Movie>) : UiModel()
        object RequestLocationPermission : UiModel()
    }

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    private  val _navigation = MutableLiveData<Event<Movie>>()
    val navigation: LiveData<Event<Movie>> = _navigation

    init {
        initScope()
    }

    private fun refresh() {
        _model.value = UiModel.RequestLocationPermission
    }

    fun onCoarsePermissionRequested() {
        launch {
            _model.value = UiModel.Loading
            _model.value = UiModel.Content(moviesRepository.findPopularMovies())
        }
    }

    fun onMovieClicked(movie: Movie) {
        _navigation.value = Event(movie)
    }

    override fun onCleared() {
        cancelScope()
        super.onCleared()
    }
}