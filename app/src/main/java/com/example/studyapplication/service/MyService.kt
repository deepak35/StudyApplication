package com.example.studyapplication.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MyService : Service() {

    inner class MyServiceBinderImpl : Binder() {
        fun getService() = this@MyService
    }

    private val binderImpl = MyServiceBinderImpl()

    private lateinit var serviceStatus : String

    override fun onBind(intent: Intent?): IBinder {
        println("onBind")
        serviceStatus = "binded"
        //onStartCommand(intent, 0, 0)
        return binderImpl
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("onStartCommand thread: ${Thread.currentThread().run{
            "$name id: $id"
        }}")

        serviceStatus = "started"

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        println("onDestroy service")
        serviceStatus = "stopped"
        super.onDestroy()
    }

    fun getServiceStatus() = serviceStatus

    companion object {
        const val defaultStatus = "Default status"
    }
}