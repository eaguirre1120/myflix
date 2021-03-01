package com.eaguirre.myflix.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eaguirre.domain.Movie
import com.eaguirre.myflix.ui.common.Event
import com.eaguirre.myflix.ui.common.ScopedViewModel
import com.eaguirre.usecases.GetPopularMovies
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainViewModel(
        private val getPopularMovies: GetPopularMovies,
        uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher){

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
            _model.value = UiModel.Content(getPopularMovies.invoke())
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