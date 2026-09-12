package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val isUser: Boolean,
    val subject: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: String = "CHAT",
    val imageUri: String? = null,
    val quizJson: String? = null
)
