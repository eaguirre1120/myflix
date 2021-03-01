package com.eaguirre.myflix

import android.app.Application
import com.eaguirre.data.MoviesRepository
import com.eaguirre.data.repository.PermissionChecker
import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.data.source.LocationDataSource
import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.myflix.data.AndroidPermissionChecker
import com.eaguirre.myflix.data.PlayServicesLocationDataSource
import com.eaguirre.myflix.data.database.MovieDatabase
import com.eaguirre.myflix.data.database.RoomDataSource
import com.eaguirre.myflix.data.server.TheMovieDbDataSource
import com.eaguirre.myflix.ui.detail.DetailActivity
import com.eaguirre.myflix.ui.detail.DetailViewModel
import com.eaguirre.myflix.ui.main.MainActivity
import com.eaguirre.myflix.ui.main.MainViewModel
import com.eaguirre.usecases.FindMovieById
import com.eaguirre.usecases.GetPopularMovies
import com.eaguirre.usecases.ToogleMovieFavorite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI(){
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    single(named("apiKey")){ androidApplication().getString(R.string.api_key)}
    single { MovieDatabase.build(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { TheMovieDbDataSource() }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<PermissionChecker> { AndroidPermissionChecker(get()) }
    single<CoroutineDispatcher> { Dispatchers.Main }
}

private val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { MoviesRepository(get(), get(), get(), get(named("apiKey"))) }
}

private val scopesModule = module {
    scope(named<MainActivity>()){
        viewModel { MainViewModel(get(), get()) }
        scoped { GetPopularMovies(get()) }
    }
    scope(named<DetailActivity>()){
        viewModel { (id: Int) -> DetailViewModel(id, get(), get(), get()) }
        scoped { FindMovieById(get()) }
        scoped { ToogleMovieFavorite(get()) }
    }
}