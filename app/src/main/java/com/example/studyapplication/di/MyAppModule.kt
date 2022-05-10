package com.example.studyapplication.di

import com.example.studyapplication.networking.MyRepository
import com.example.studyapplication.networking.MyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class MyAppModule {

    @Singleton
    @Binds
    abstract fun getAPIRepository(myRepositoryImpl: MyRepositoryImpl) : MyRepository
}