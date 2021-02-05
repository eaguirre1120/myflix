package com.eaguirre.myflix.data

import com.eaguirre.domain.Movie
import com.eaguirre.myflix.data.database.Movie as DomainMovie
import com.eaguirre.myflix.data.server.Movie as ServerMovie

fun Movie.toRoomMovie(): DomainMovie = DomainMovie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        favorite
)

fun DomainMovie.toDomainMovie() : Movie = Movie(
        id,
        title,
        overview,
        releaseDate,
        posterPath,
        backdropPath,
        originalLanguage,
        originalTitle,
        popularity,
        voteAverage,
        favorite
)

fun ServerMovie.toDomainMovie(): Movie = Movie(
    0,
        title,
        overview,
        release_date,
        poster_path,
        backdrop_path ?: poster_path,
        original_language,
        original_title,
        popularity,
        vote_average,
        false
)