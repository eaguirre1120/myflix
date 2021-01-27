package com.eaguirre.myflix.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityDetailBinding
import com.eaguirre.myflix.model.database.Movie
import com.eaguirre.myflix.model.server.MoviesRepository
import com.eaguirre.myflix.ui.common.app
import com.eaguirre.myflix.ui.common.getViewModel


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object{
        const val EXTRA_MOVIE = "DetailActivity:movie"
    }
    private lateinit var viewModel: DetailViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movieId = intent.getIntExtra(EXTRA_MOVIE, 0)
        if (movieId != null) {
            viewModel = getViewModel {
                DetailViewModel(movieId, MoviesRepository(app))
            }//ViewModelProvider(this, DetailViewModelFactory(movie))[DetailViewModel::class.java]
        }

        viewModel.model.observe(this, Observer(::updateUi))

        binding.fab.setOnClickListener {
//            Toast.makeText(this, "Button like clicked!", Toast.LENGTH_LONG).show()
            viewModel.onMovieFavoriteClicked()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUi(model: DetailViewModel.UiModel) {
        val movie = model.movie
        title = movie.title
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780/${movie.backdropPath}")
                .into(binding.backdrop)
        binding.summary.text = movie.overview + movie.overview + movie.overview + movie.overview + movie.overview + movie.overview
        binding.detailinfo.setMovie(movie)
        //setDetailInfo(binding.detailinfo, movie)
        val icon = if (movie.favorite) R.drawable.ic_favorite_border_checked else R.drawable.ic_favorite_border_unchecked
        binding.fab.setImageDrawable(getDrawable(icon))
    }
}
