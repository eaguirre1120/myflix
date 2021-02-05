package com.eaguirre.myflix.di

import android.app.Application
import androidx.room.Room
import com.eaguirre.data.repository.PermissionChecker
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.data.source.LocationDataSource
import com.eaguirre.data.source.RemoteDataSource
import com.eaguirre.myflix.R
import com.eaguirre.myflix.data.AndroidPermissionChecker
import com.eaguirre.myflix.data.PlayServicesLocationDataSource
import com.eaguirre.myflix.data.database.MovieDatabase
import com.eaguirre.myflix.data.database.RoomDataSource
import com.eaguirre.myflix.data.server.TheMovieDbDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    @Named("apiKey")
    fun apiKeyProvider(app: Application): String = app.getString(R.string.api_key)

    @Provides
    @Singleton
    fun databaseProvider(app: Application) = Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie_db"
    ).build()

    @Provides
    fun localDataSourceProvider(db: MovieDatabase): LocalDataSource = RoomDataSource(db)

    @Provides
    fun remoteDataSourceProvider(): RemoteDataSource = TheMovieDbDataSource()

    @Provides
    fun locationDataSourceProvider(app: Application): LocationDataSource =
            PlayServicesLocationDataSource(app)

    @Provides
    fun permissionCheckerProvider(app: Application): PermissionChecker =
            AndroidPermissionChecker(app)

}