package com.example.data.model

enum class MessageType {
    CHAT,
    QUIZ,
    EXPLANATION,
    SYSTEM
}

data class ChatMessage(
    val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val subject: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: MessageType = MessageType.CHAT,
    val imageUri: String? = null,
    val quizData: QuizData? = null
)
