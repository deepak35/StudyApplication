package com.example.studyapplication.di

import android.app.Application
import android.content.Context
import android.os.Handler
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: Application) : Context = application

}