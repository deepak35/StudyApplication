package com.example.studyapplication.model

import java.util.ArrayList

data class SubjectData(
    val subject: String,
    val marks: Int
)

fun getSubjectData() = ArrayList<SubjectData>().apply {
    addAll(
        Array(20){
            SubjectData(subject = "subject$it", marks = it + 100)
        }
    )
}
