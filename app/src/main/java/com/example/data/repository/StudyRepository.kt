package com.example.data.repository

import android.content.Context
import com.example.data.local.ChatDao
import com.example.data.local.ChatMessageEntity
import com.example.data.local.StudyDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.ChatMessage
import com.example.data.model.MessageType
import com.example.data.model.QuizData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StudyRepository(
    private val chatDao: ChatDao
) {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val quizAdapter = moshi.adapter(QuizData::class.java)

    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages().map { entities ->
        entities.map { entity ->
            val quiz = entity.quizJson?.let {
                try {
                    quizAdapter.fromJson(it)
                } catch (e: Exception) {
                    null
                }
            }
            ChatMessage(
                id = entity.id,
                text = entity.text,
                isUser = entity.isUser,
                subject = entity.subject,
                timestamp = entity.timestamp,
                messageType = try {
                    MessageType.valueOf(entity.messageType)
                } catch (e: Exception) {
                    MessageType.CHAT
                },
                imageUri = entity.imageUri,
                quizData = quiz
            )
        }
    }

    suspend fun insertMessage(
        text: String,
        isUser: Boolean,
        subject: String,
        messageType: MessageType = MessageType.CHAT,
        imageUri: String? = null,
        quizData: QuizData? = null
    ): Long = withContext(Dispatchers.IO) {
        val quizJson = quizData?.let { quizAdapter.toJson(it) }
        val entity = ChatMessageEntity(
            text = text,
            isUser = isUser,
            subject = subject,
            timestamp = System.currentTimeMillis(),
            messageType = messageType.name,
            imageUri = imageUri,
            quizJson = quizJson
        )
        chatDao.insertMessage(entity)
    }

    suspend fun updateQuizAnswer(
        messageId: Long,
        questionIndex: Int,
        selectedOptionIndex: Int,
        currentQuizData: QuizData
    ) = withContext(Dispatchers.IO) {
        val updatedQuestions = currentQuizData.questions.mapIndexed { idx, q ->
            if (idx == questionIndex) {
                q.copy(userSelectedIndex = selectedOptionIndex)
            } else {
                q
            }
        }
        val updatedQuiz = currentQuizData.copy(questions = updatedQuestions)
        val quizJson = quizAdapter.toJson(updatedQuiz)

        val entity = ChatMessageEntity(
            id = messageId,
            text = "📝 ${updatedQuiz.topic} Quiz",
            isUser = false,
            subject = updatedQuiz.subject,
            messageType = MessageType.QUIZ.name,
            quizJson = quizJson
        )
        chatDao.updateMessage(entity)
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        chatDao.clearAll()
    }

    suspend fun ensureWelcomeMessage(language: AppLanguage = AppLanguage.ENGLISH) = withContext(Dispatchers.IO) {
        val welcomeText = when (language) {
            AppLanguage.ENGLISH -> "👋 Hi! I'm SUMIT AI, your smart learning companion. Pick a subject above, ask any question, or tap Make Quiz / Explain Topic to get started!"
            AppLanguage.HINDI -> "👋 नमस्ते! मैं SUMIT AI हूँ, आपका स्मार्ट स्टडी साथी। ऊपर से विषय चुनें, कोई भी प्रश्न पूछें या क्विज़ बनाएं!"
            AppLanguage.MARATHI -> "👋 नमस्कार! मी SUMIT AI आहे, तुमचा स्मार्ट अभ्यास मित्र. वरील विषय निवडा, कोणताही प्रश्न विचारा किंवा सराव क्विझ सुरू करा!"
        }
        insertMessage(
            text = welcomeText,
            isUser = false,
            subject = "Math",
            messageType = MessageType.CHAT
        )
    }

    companion object {
        fun create(context: Context): StudyRepository {
            val db = StudyDatabase.getDatabase(context)
            return StudyRepository(db.chatDao())
        }
    }
}
