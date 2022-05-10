package com.example.studyapplication.networking

import com.example.studyapplication.model.ActivityResponse
import retrofit2.Call

interface MyRepository {

    suspend fun getActivity() : ActivityResponse
}