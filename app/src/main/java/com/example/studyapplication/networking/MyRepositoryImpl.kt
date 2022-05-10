package com.example.studyapplication.networking

import com.example.studyapplication.example.dispatcherIO
import com.example.studyapplication.model.ActivityResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(private val boredAPIService: BoredAPIService) :
    MyRepository {
    override suspend fun getActivity() = withContext(dispatcherIO) {
        boredAPIService.getActivity()
    }
}
