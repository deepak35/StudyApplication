package com.example.studyapplication.example

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime

val dispatcherIO = Dispatchers.IO
val dispatcherMain = Dispatchers.Main.immediate

var coroutineNum = 1

fun startNewThread(): String {
    val tc = ThreadClass()
    tc.start()
    tc.join()
    return "sdads"
}

suspend fun suspendF1(): String {
    val ld = MutableLiveData(1)
    delay(2000L)
    return "suspendF1"
}

suspend fun withScope() {
    coroutineScope {
        println("inside scope, launching new coroutine $this")
        val newjob = launch {
            println("inside coroutine before scope fn call")
            val tState = suspendF1()
            println("thread state $tState")
        }
//        newjob.join()
//        delay(2000L)
        println("outside coroutine launch")
    }
}

class ThreadClass : Thread() {
    override fun run() {
        println("inside new thread : ${currentThread()}")
        sleep(10000)
        println("after sleep")
    }
}

class Datas : testi {
    fun tryy() {

    }
}

interface testi {
    private fun ifun() {
        print("i fun")
    }
}


fun main() {
    print('a' == 'A')
}

fun balancedSum(arr: Array<Int>): Int {
    val leftSum = Array(arr.size) {0}
    val rightSum = Array(arr.size) {0}

    var currSum = 0

    for (i in arr.indices) {
        currSum += arr[i]
        leftSum[i] = currSum
    }

    currSum = 0
    for (i in arr.lastIndex downTo 0) {
        currSum += arr[i]
        rightSum[i] = currSum
    }

    for (i in leftSum.indices){
        if(leftSum[i] == rightSum[i])
            return i
    }

    return -1


}

fun modifyArray(arr: Array<Int>): Long {
    val mid = (arr[0] + arr[arr.lastIndex]) / 2

    var ans = 0

    for(i in 1 until arr.lastIndex) {
        ans += abs(mid - arr[i])
    }

    return ans.toLong()
}

fun postorder(root: Node?): List<Int> {

    val ansList = mutableListOf<Int>()

    root?.children?.forEach {
        it?.let { node ->
            ansList.plus(postorder(node))
        }
    }
    return if (root != null) {
        ansList.plus(root.`val`)
    }
    else emptyList()
}

class Node(var `val`: Int) {
    var children: List<Node?> = listOf()
}

suspend fun fa(): String {
    delay(2000)
    println("fa")

    return "fa"
}

suspend fun fb(): String {
    delay(2000)
    println("fb")
    return "fb"
}


suspend fun scopefn() {
    coroutineScope {
        launch {
            delay(2000)
            println("inside launch")
        }
        println("out launch")
    }
    println("out scope")
}