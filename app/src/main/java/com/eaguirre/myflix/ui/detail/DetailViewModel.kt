package com.eaguirre.myflix.ui.detail


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eaguirre.domain.Movie
import com.eaguirre.myflix.ui.common.ScopedViewModel
import com.eaguirre.usecases.FindMovieById
import com.eaguirre.usecases.ToogleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch


class DetailViewModel(
    private val movieId: Int,
    private val findMovieById: FindMovieById,
    private val toogleMovieFavorite: ToogleMovieFavorite,
    uiDispatcher: CoroutineDispatcher
    ) : ScopedViewModel(uiDispatcher) {

    class UiModel(val movie: Movie)

    private val _model = MutableLiveData<UiModel>()
    val model: LiveData<UiModel>
        get() {
            if (_model.value == null) findMovie()
            return _model
        }
    private fun findMovie() = launch {
        _model.value = UiModel(findMovieById.invoke(movieId))
    }

    fun onMovieFavoriteClicked() = launch {
        _model.value?.movie?.let {
            _model.value = UiModel(toogleMovieFavorite.invoke(it))
        }
    }
}