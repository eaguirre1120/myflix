package com.eaguirre.myflix.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.eaguirre.data.MoviesRepository
import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityDetailBinding
import com.eaguirre.myflix.data.AndroidPermissionChecker
import com.eaguirre.myflix.data.PlayServicesLocationDataSource
import com.eaguirre.myflix.data.database.RoomDataSource
import com.eaguirre.myflix.data.server.TheMovieDbDataSource
import com.eaguirre.myflix.ui.common.app
import com.eaguirre.myflix.ui.common.getViewModel
import com.eaguirre.myflix.ui.detail.DetailViewModel.*
import com.eaguirre.usecases.FindMovieById
import com.eaguirre.usecases.ToogleMovieFavorite
import kotlinx.android.synthetic.main.activity_detail.view.*


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var component: DetailActivityComponent
    private val viewModel by lazy { getViewModel{ component.detailViewModel } }

    companion object{
        const val EXTRA_MOVIE = "DetailActivity:movie"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        component = app.component.plus(DetailActivityModule(intent.getIntExtra(EXTRA_MOVIE, -1)))

        /*val movieId = intent.getIntExtra(EXTRA_MOVIE, 0)
        if (movieId != null) {
            viewModel = getViewModel {
                val moviesRepository = MoviesRepository(
                    RoomDataSource(app.db),
                    TheMovieDbDataSource(),
                    RegionRepository(
                        PlayServicesLocationDataSource(app),
                        AndroidPermissionChecker(app)
                    ),
                    app.getString(R.string.api_key)
                )

                DetailViewModel(
                    movieId,
                    FindMovieById(moviesRepository),
                    ToogleMovieFavorite(moviesRepository)
                )
            }
        //ViewModelProvider(this, DetailViewModelFactory(movie))[DetailViewModel::class.java]
        }*/

        viewModel.model.observe(this, Observer(::updateUi))

        binding.fab.setOnClickListener {
//            Toast.makeText(this, "Button like clicked!", Toast.LENGTH_LONG).show()
            viewModel.onMovieFavoriteClicked()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUi(model: UiModel) {
        val movie = model.movie
        title = movie.title
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780/${movie.backdropPath}")
                .into(binding.backdrop)
        binding.summary.text = movie.overview
        binding.detailinfo.setMovie(movie)
        //setDetailInfo(binding.detailinfo, movie)
        val icon = if (movie.favorite) R.drawable.ic_favorite_border_checked else R.drawable.ic_favorite_border_unchecked
        binding.fab.setImageDrawable( ContextCompat.getDrawable(this@DetailActivity,icon))
    }
}
