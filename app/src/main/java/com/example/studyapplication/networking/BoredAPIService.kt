package com.example.studyapplication.networking

import com.example.studyapplication.model.ActivityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface BoredAPIService {

    @GET("/activity/")
    suspend fun getActivity() : ActivityResponse
}