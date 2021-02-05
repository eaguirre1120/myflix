package com.eaguirre.myflix

import android.app.Application
import com.eaguirre.myflix.di.DaggerMyFlixComponent
import com.eaguirre.myflix.di.MyFlixComponent

class MoviesApp: Application() {
    lateinit var component: MyFlixComponent
    private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerMyFlixComponent
                .factory()
                .create(this)
    }
}