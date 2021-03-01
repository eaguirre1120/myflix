package com.eaguirre.miflix.ui

import com.eaguirre.data.repository.PermissionChecker
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.data.source.LocationDataSource
import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.domain.Movie
import com.eaguirre.myflix.dataModule
import com.eaguirre.testshared.mockedMovie
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initMockedDi(vararg modules: Module){
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}

private val mockedAppModule = module{
    single(named("apiKey")) { "225256" }
    single<LocalDataSource> { FakeLocalDataSource() }
    single<LocationDataSource> { FakeLocationDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single<PermissionChecker> { FakePermissionChecker() }
    single { Dispatchers.Unconfined }
}

val defaultFakeMovies: List<Movie> = listOf(
        mockedMovie.copy(1),
        mockedMovie.copy(2),
        mockedMovie.copy(3),
        mockedMovie.copy(4)
)


class FakeLocalDataSource: LocalDataSource{
    var movies: List<Movie> = emptyList()
    override suspend fun isEmpty() = movies.isEmpty()

    override suspend fun saveMovies(movies: List<Movie>) {
        this.movies = movies
    }

    override suspend fun getPopularMovies(): List<Movie> = movies

    override suspend fun findById(id: Int): Movie = movies.first{it.id == id}

    override suspend fun update(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie
    }

}

class FakeRemoteDataSource: RemoteDataSource{
    val movies = defaultFakeMovies
    override suspend fun getPopularMovies(apiKey: String, region: String) = movies
}

class FakeLocationDataSource : LocationDataSource{
    var location = "US"
    override suspend fun findLastRegion(): String? = location

}

class FakePermissionChecker : PermissionChecker {
    var permissionGranted = true
    override suspend fun check(permission: PermissionChecker.Permission): Boolean = permissionGranted
}