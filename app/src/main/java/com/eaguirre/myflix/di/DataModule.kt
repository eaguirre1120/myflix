package com.eaguirre.myflix.di

import com.eaguirre.data.MoviesRepository
import com.eaguirre.data.repository.PermissionChecker
import com.eaguirre.data.repository.RegionRepository
import com.eaguirre.data.source.LocalDataSource
import com.eaguirre.data.source.LocationDataSource
import com.eaguirre.data.source.RemoteDataSource
import dagger.Module

import dagger.Provides
import javax.inject.Named

@Module
class DataModule {
    @Provides
    fun regionRepositoryProvider(
            locationDataSource: LocationDataSource,
            permissionChecker: PermissionChecker
    ) = RegionRepository(locationDataSource, permissionChecker)

    @Provides
    fun moviesRepository(
            localDataSource: LocalDataSource,
            remoteDataSource: RemoteDataSource,
            regionRepository: RegionRepository,
            @Named("apiKey") apiKey: String
    ) = MoviesRepository(localDataSource,remoteDataSource,regionRepository,apiKey)
}