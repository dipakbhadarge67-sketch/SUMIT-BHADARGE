package com.example.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val userSelectedIndex: Int? = null
)

@JsonClass(generateAdapter = true)
data class QuizData(
    val subject: String,
    val topic: String,
    val questions: List<QuizQuestion>
)
