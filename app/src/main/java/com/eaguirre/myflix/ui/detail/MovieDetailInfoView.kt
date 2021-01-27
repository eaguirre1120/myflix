package com.eaguirre.myflix.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.eaguirre.myflix.R
import com.eaguirre.myflix.model.database.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@SuppressLint("AppCompatCustomView")
class MovieDetailInfoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr){

    @RequiresApi(Build.VERSION_CODES.O)
    fun setMovie(movie: Movie) = with(movie) {
        val dateRelease = LocalDate.parse(movie.releaseDate)
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        text = buildSpannedString {
            appendInfo(this, R.string.original_language, movie.originalLanguage)
            appendInfo(this, R.string.original_title, movie.originalTitle)
            appendInfo(this, R.string.release_date, dateRelease.format(formatter))
            appendInfo(this, R.string.popularity, movie.popularity.toString())
            appendInfo(this, R.string.vote_average, movie.voteAverage.toString())
        }
    }

    private fun appendInfo(builder: SpannableStringBuilder, stringRes:Int, value: String) {
        builder.bold {
            builder.append(resources.getString(stringRes))
            builder.append(": ")
        }

        builder.appendLine( value )
    }
}