package com.example.studyapplication.di

import android.app.Application
import android.os.Handler
import androidx.work.WorkManager
import com.example.studyapplication.MainActivity
import com.example.studyapplication.MyApplication
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MyAppModule::class, NetworkModule::class, ApplicationModule::class])
interface MyApplicationComponent {
    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build() : MyApplicationComponent

        @BindsInstance
        fun bindsApplication(application: Application) : Builder

        @BindsInstance
        fun bindsWorkManager(workManager: WorkManager) : Builder
    }
}