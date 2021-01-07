package com.eaguirre.myflix.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.bumptech.glide.Glide
import com.eaguirre.myflix.R
import com.eaguirre.myflix.databinding.ActivityDetailBinding
import com.eaguirre.myflix.model.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MOVIE = "DetailActivity:movie"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra<Movie>(EXTRA_MOVIE)
        if (movie != null) {
            title = movie.title
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780/${movie.backdrop_path}")
                .into(binding.backdrop)
            binding.summary.text = movie.overview
            setDetailInfo(binding.detailinfo, movie)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDetailInfo(detailinfo: TextView, movie: Movie) {
        val dateRelease = LocalDate.parse(movie.release_date)
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        detailinfo.text = buildSpannedString {
            appendInfo(this, R.string.original_language, movie.original_language)
            appendInfo(this, R.string.original_title, movie.original_title)
            appendInfo(this, R.string.release_date, dateRelease.format(formatter))
            appendInfo(this, R.string.popularity, movie.popularity.toString())
            appendInfo(this, R.string.vote_average, movie.vote_count.toString())

            /*bold { append("Original title: ") }
            appendLine(movie.original_title)

            bold { append("Relesase date: ") }
            appendLine(dateRelease.format(formatter))

            bold { append("Popularity: ") }
            appendLine(movie.popularity.toString())

            bold { append("Vote average: ") }
            appendLine(movie.vote_average.toString())*/
        }
    }

    private fun appendInfo(builder: SpannableStringBuilder,stringRes:Int, value: String) {
        builder.bold {
            builder.append(getString(stringRes))
            builder.append(": ")
        }

        builder.appendLine( value )
    }

}
