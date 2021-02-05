package com.eaguirre.myflix.data

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.eaguirre.data.source.LocationDataSource
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(application: Application) : LocationDataSource {
    private val geocoder = Geocoder(application)
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)


    @SuppressLint("MissingPermission")
    override suspend fun findLastRegion(): String? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener{
                    continuation.resume(it.result.toRegion())
                }
        }
    private fun Location?.toRegion(): String? {
        val address = this?.let{
            geocoder.getFromLocation(it.latitude,it.longitude,1)
        }
        return address?.firstOrNull()?.countryCode
    }
}
