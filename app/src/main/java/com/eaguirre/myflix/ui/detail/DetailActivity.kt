package com.eaguirre.myflix.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.eaguirre.myflix.databinding.ActivityDetailBinding
import com.eaguirre.myflix.model.Movie


class DetailActivity : AppCompatActivity(), DetailPresenter.View {

    private lateinit var binding: ActivityDetailBinding

    companion object{
        const val EXTRA_MOVIE = "DetailActivity:movie"
    }
    private val presenter = DetailPresenter()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.fab.setOnClickListener {
            Toast.makeText(this, "Button like clicked!", Toast.LENGTH_LONG).show()
        }

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (movie != null) {
            presenter.onCreate(this, movie)
        }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateUi(movie: Movie) {
        title = movie.title
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780/${movie.backdrop_path}")
                .into(binding.backdrop)
        binding.summary.text = movie.overview + movie.overview + movie.overview + movie.overview + movie.overview + movie.overview
        binding.detailinfo.setMovie(movie)
        //setDetailInfo(binding.detailinfo, movie)
    }

    /*@RequiresApi(Build.VERSION_CODES.O)
    private fun setDetailInfo(detailinfo: TextView, movie: Movie) {
        val dateRelease = LocalDate.parse(movie.release_date)
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        detailinfo.text = buildSpannedString {
            appendInfo(this, R.string.original_language, movie.original_language)
            appendInfo(this, R.string.original_title, movie.original_title)
            appendInfo(this, R.string.release_date, dateRelease.format(formatter))
            appendInfo(this, R.string.popularity, movie.popularity.toString())
            appendInfo(this, R.string.vote_average, movie.vote_count.toString())
        }
    }

    private fun appendInfo(builder: SpannableStringBuilder,stringRes:Int, value: String) {
        builder.bold {
            builder.append(getString(stringRes))
            builder.append(": ")
        }

        builder.appendLine( value )
    }*/

}
