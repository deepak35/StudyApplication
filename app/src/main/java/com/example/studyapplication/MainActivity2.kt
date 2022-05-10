package com.example.studyapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("B onCreate")
        setContentView(R.layout.activity_main2)
    }

    override fun onStart() {
        super.onStart()
        println("B onStart")
    }

    override fun onResume() {
        super.onResume()
        println("B onResume")
    }

    override fun onPause() {
        super.onPause()
        println("B onPause")
        finish()
    }

    override fun onStop() {
        super.onStop()
        println("B onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("B onSaveInstanceState")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("B onDestroy")
    }
}