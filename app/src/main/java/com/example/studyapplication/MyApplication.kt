package com.example.studyapplication

import android.app.Application
import androidx.work.WorkManager
import com.example.studyapplication.di.DaggerMyApplicationComponent
import com.example.studyapplication.di.MyApplicationComponent

class MyApplication : Application() {

    private lateinit var workManager: WorkManager

    private lateinit var appComponent: MyApplicationComponent

    override fun onCreate() {
        super.onCreate()
        workManager = WorkManager.getInstance(this)
        setupDI()
    }

    private fun setupDI () {
        appComponent = DaggerMyApplicationComponent.builder()
            .bindsApplication(this)
            .bindsWorkManager(workManager)
            .build()
    }

    fun getAppComponent(): MyApplicationComponent = appComponent
}