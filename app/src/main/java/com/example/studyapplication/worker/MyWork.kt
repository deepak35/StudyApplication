package com.example.studyapplication.worker

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.material.snackbar.Snackbar

class MyWork(private val context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    private val handler = Handler(Looper.getMainLooper())

    private var currCount = 0

    override fun doWork(): Result {
        println("WORK STARTED with id: ${this.id} on thread ${Thread.currentThread()}")
        currCount += 1
        handler.post {
            Toast.makeText(context, "Worker $currCount started!!", Toast.LENGTH_SHORT).show()
        }
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        println("WORK STOPPED with id: ${this.id} on thread ${Thread.currentThread()}")
    }

}