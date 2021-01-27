package com.eaguirre.myflix.model.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieDb {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(TheMovieDbService::class.java)

}