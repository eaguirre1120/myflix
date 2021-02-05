package com.eaguirre.myflix.di

import android.app.Application
import com.eaguirre.myflix.ui.detail.DetailActivityComponent
import com.eaguirre.myflix.ui.detail.DetailActivityModule
import com.eaguirre.myflix.ui.main.MainActivityComponent
import com.eaguirre.myflix.ui.main.MainActivityModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface MyFlixComponent {

    fun plus(module: MainActivityModule): MainActivityComponent
    fun plus(module: DetailActivityModule): DetailActivityComponent

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance app: Application): MyFlixComponent
    }
}