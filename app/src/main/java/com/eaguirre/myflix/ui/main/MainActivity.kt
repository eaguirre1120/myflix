package com.eaguirre.myflix.ui.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.eaguirre.myflix.ui.common.PermissionRequester
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.ui.common.startActivity
import com.eaguirre.myflix.ui.detail.DetailActivity
import com.eaguirre.myflix.ui.main.MainViewModel.UiModel
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ScopeActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var moviesAdapter : MoviesAdapter
    private val coarsePermissionRequester = PermissionRequester(this, ACCESS_COARSE_LOCATION)

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        /*viewModel = getViewModel {
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
        }*///ViewModelProvider( this, MainViewModelFactory(MoviesRepository(this)))[MainViewModel::class.java]

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
