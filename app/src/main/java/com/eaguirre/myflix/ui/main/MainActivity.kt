package com.eaguirre.myflix.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.eaguirre.data.MoviesRepository
import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.myflix.PermissionRequester
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.model.AndroidPermissionChecker
import com.eaguirre.myflix.model.PlayServicesLocationDataSource
import com.eaguirre.myflix.model.database.RoomDataSource
import com.eaguirre.myflix.model.server.TheMovieDbDataSource
import com.eaguirre.myflix.ui.common.app
import com.eaguirre.myflix.ui.common.getViewModel
import com.eaguirre.myflix.ui.common.startActivity
import com.eaguirre.myflix.ui.detail.DetailActivity
import com.eaguirre.myflix.ui.main.MainViewModel.UiModel
import com.eaguirre.usecases.GetPopularMovies


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  viewModel : MainViewModel
    private lateinit var moviesAdapter : MoviesAdapter
    private val coarsePermissionRequester = PermissionRequester(this, ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        viewModel = getViewModel {
            MainViewModel(
                    GetPopularMovies(
                        MoviesRepository(
                            RoomDataSource(app.db),
                            TheMovieDbDataSource(),
                            RegionRepository(
                                PlayServicesLocationDataSource(app),
                                AndroidPermissionChecker(app)
                            ),
                            getString(R.string.api_key)
                        )
                    )
            )
        } //ViewModelProvider( this, MainViewModelFactory(MoviesRepository(this)))[MainViewModel::class.java]

        moviesAdapter = MoviesAdapter(viewModel::onMovieClicked)
        binding.recyclerMovies.adapter = moviesAdapter

        viewModel.model.observe(this, Observer(::updateUi))
        viewModel.navigation.observe(this, Observer {
            event ->
            event.getContentIfNotHandled()?.let{
                startActivity<DetailActivity> {
                    putExtra(DetailActivity.EXTRA_MOVIE, it.id)
                }
            }
        })

    }

    private fun updateUi(model: UiModel) {

        binding.progressbar.visibility = if (model == UiModel.Loading) View.VISIBLE else View.GONE

        when(model){
            is UiModel.Content -> {
                moviesAdapter.movies = model.movies
                moviesAdapter.notifyDataSetChanged()
            }
            UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
        }

    }
}
