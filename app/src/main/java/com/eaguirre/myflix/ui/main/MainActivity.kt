package com.eaguirre.myflix.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.eaguirre.myflix.databinding.ActivityMainBinding
import com.eaguirre.myflix.model.Movie
import com.eaguirre.myflix.model.MoviesRepository
import com.eaguirre.myflix.ui.detail.DetailActivity


class MainActivity : AppCompatActivity(), MainPresenter.View {

    private lateinit var binding: ActivityMainBinding
    private val presenter by lazy {  MainPresenter(MoviesRepository(this)) }

    private val moviesAdapter = MoviesAdapter(emptyList()) { movie ->
        presenter.onMovieClicked(movie)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)//El objeto toma el nombre del layout
        setContentView(binding.root)

        presenter.onCreate(this)

        binding.recyclerMovies.adapter = moviesAdapter

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        binding.progressbar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressbar.visibility = View.GONE
    }

    override fun updateData(movies: List<Movie>) {
        moviesAdapter.movies = movies
        moviesAdapter.notifyDataSetChanged()
    }

    override fun navigateTo(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie)
        startActivity(intent)
    }
}
