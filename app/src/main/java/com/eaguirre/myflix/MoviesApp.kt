package com.eaguirre.myflix

import android.app.Application

class MoviesApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}