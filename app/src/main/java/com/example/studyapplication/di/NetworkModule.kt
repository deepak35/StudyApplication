package com.example.studyapplication.di

import com.example.studyapplication.networking.BoredAPIService
import com.example.studyapplication.networking.RetrofitClient
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun getBoredApiService() : BoredAPIService {
        return RetrofitClient().getRetrofitClient().create(BoredAPIService::class.java)
    }
}