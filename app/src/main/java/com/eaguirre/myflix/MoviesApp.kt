package com.eaguirre.myflix

import android.app.Application
import androidx.room.Room
import com.eaguirre.myflix.model.database.MovieDatabase

class MoviesApp: Application() {
    lateinit var db: MovieDatabase
    private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            MovieDatabase::class.java,
            "movie_db"
        ).build()
    }
}