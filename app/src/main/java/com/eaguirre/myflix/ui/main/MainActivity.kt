package com.eaguirre.myflix.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eaguirre.myflix.PermissionRequester
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.model.Movie
import com.eaguirre.myflix.model.MoviesRepository
import com.eaguirre.myflix.ui.common.getViewModel
import com.eaguirre.myflix.ui.common.startActivity
import com.eaguirre.myflix.ui.detail.DetailActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  viewModel : MainViewModel
    private lateinit var moviesAdapter : MoviesAdapter
    private val coarsePermissionRequester = PermissionRequester(this, ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        viewModel = getViewModel { MainViewModel(MoviesRepository(application)) } //ViewModelProvider( this, MainViewModelFactory(MoviesRepository(this)))[MainViewModel::class.java]

        moviesAdapter = MoviesAdapter(viewModel::onMovieClicked)
        binding.recyclerMovies.adapter = moviesAdapter

        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.navigation.observe(this, Observer {
            event ->
            event.getContentIfNotHandled()?.let{
                startActivity<DetailActivity> {
                    putExtra(DetailActivity.EXTRA_MOVIE, it)
                }
            }
        })

    }

    private fun updateUi(model: MainViewModel.UiModel) {

        binding.progressbar.visibility = if (model == MainViewModel.UiModel.Loading) View.VISIBLE else View.GONE

        when(model){
            is MainViewModel.UiModel.Content -> {
                moviesAdapter.movies = model.movies
                moviesAdapter.notifyDataSetChanged()
            }
            MainViewModel.UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
        }


    }
}
