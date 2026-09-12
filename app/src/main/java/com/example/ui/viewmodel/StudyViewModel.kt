package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.model.ChatMessage
import com.example.data.model.MessageType
import com.example.data.model.QuizData
import com.example.data.model.Subject
import com.example.data.remote.StudyTutorEngine
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudyUiState(
    val selectedSubject: Subject = Subject.MATH,
    val selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    val showLanguageDialog: Boolean = false,
    val isInitialLanguagePrompt: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val isGenerating: Boolean = false,
    val inputText: String = "",
    val attachedImageUri: Uri? = null,
    val attachedBitmap: Bitmap? = null,
    val showExplainDialog: Boolean = false,
    val showMakeQuizDialog: Boolean = false,
    val showClearConfirmDialog: Boolean = false,
    val isApiKeyActive: Boolean = false,
    val infoMessage: String? = null
)

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StudyRepository.create(application)
    private val tutorEngine = StudyTutorEngine()
    private val prefs = application.getSharedPreferences("sumit_ai_prefs", Context.MODE_PRIVATE)

    private val initialLanguage: AppLanguage = run {
        val savedCode = prefs.getString("pref_language", null)
        AppLanguage.fromCode(savedCode)
    }

    private val hasChosenLanguage: Boolean = prefs.getBoolean("key_language_selected", false)

    private val _uiState = MutableStateFlow(
        StudyUiState(
            selectedLanguage = initialLanguage,
            showLanguageDialog = !hasChosenLanguage,
            isInitialLanguagePrompt = !hasChosenLanguage,
            isApiKeyActive = tutorEngine.isApiKeyConfigured()
        )
    )

    val uiState: StateFlow<StudyUiState> = combine(
        _uiState,
        repository.allMessages
    ) { state, messages ->
        state.copy(messages = messages)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _uiState.value
    )

    init {
        viewModelScope.launch {
            repository.allMessages.collect { list ->
                if (list.isEmpty()) {
                    repository.ensureWelcomeMessage(_uiState.value.selectedLanguage)
                }
            }
        }
    }

    fun openLanguageDialog() {
        _uiState.update { it.copy(showLanguageDialog = true, isInitialLanguagePrompt = false) }
    }

    fun closeLanguageDialog() {
        _uiState.update { it.copy(showLanguageDialog = false) }
    }

    fun selectLanguage(language: AppLanguage) {
        prefs.edit()
            .putString("pref_language", language.code)
            .putBoolean("key_language_selected", true)
            .apply()

        _uiState.update {
            it.copy(
                selectedLanguage = language,
                showLanguageDialog = false,
                isInitialLanguagePrompt = false
            )
        }

        viewModelScope.launch {
            val announcement = when (language) {
                AppLanguage.ENGLISH -> "🌐 **Language Set: English**\nSUMIT AI is ready to help you learn! Ask any question or select a subject above."
                AppLanguage.HINDI -> "🌐 **भाषा चुनी गई: हिन्दी**\nSUMIT AI आपकी पढ़ाई में मदद के लिए तैयार है! कोई भी सवाल पूछें या विषय चुनें।"
                AppLanguage.MARATHI -> "🌐 **भाषा निवडली: मराठी**\nSUMIT AI तुमच्या अभ्यासात मदत करण्यासाठी सज्ज आहे! कोणताही प्रश्न विचारा किंवा विषय निवडा."
            }
            repository.insertMessage(
                text = announcement,
                isUser = false,
                subject = _uiState.value.selectedSubject.title,
                messageType = MessageType.SYSTEM
            )
        }
    }

    fun selectSubject(subject: Subject) {
        val currentLang = _uiState.value.selectedLanguage
        _uiState.update { it.copy(selectedSubject = subject) }
        viewModelScope.launch {
            val headerText = when (currentLang) {
                AppLanguage.ENGLISH -> "📚 **Subject Selected: ${subject.icon} ${subject.localizedTitle(currentLang)}**\n${subject.localizedTip(currentLang)}"
                AppLanguage.HINDI -> "📚 **विषय चुना गया: ${subject.icon} ${subject.localizedTitle(currentLang)}**\n${subject.localizedTip(currentLang)}"
                AppLanguage.MARATHI -> "📚 **विषय निवडला: ${subject.icon} ${subject.localizedTitle(currentLang)}**\n${subject.localizedTip(currentLang)}"
            }
            repository.insertMessage(
                text = headerText,
                isUser = false,
                subject = subject.title,
                messageType = MessageType.SYSTEM
            )
        }
    }

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun attachPhoto(uri: Uri?) {
        if (uri == null) {
            _uiState.update { it.copy(attachedImageUri = null, attachedBitmap = null) }
            return
        }

        try {
            val contentResolver = getApplication<Application>().contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            _uiState.update {
                it.copy(
                    attachedImageUri = uri,
                    attachedBitmap = bitmap
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(infoMessage = "Could not load image: ${e.message}")
            }
        }
    }

    fun removeAttachedPhoto() {
        _uiState.update { it.copy(attachedImageUri = null, attachedBitmap = null) }
    }

    fun sendQuestion(questionText: String? = null) {
        val currentState = _uiState.value
        val text = (questionText ?: currentState.inputText).trim()
        val attachedBitmap = currentState.attachedBitmap
        val attachedUriString = currentState.attachedImageUri?.toString()
        val lang = currentState.selectedLanguage

        if (text.isEmpty() && attachedBitmap == null) return

        val promptToSend = if (text.isNotEmpty()) text else when (lang) {
            AppLanguage.ENGLISH -> "Please solve and explain this homework problem in the image."
            AppLanguage.HINDI -> "कृपया इस चित्र में दिए गए गृहकार्य प्रश्न को हल करें और समझाएं।"
            AppLanguage.MARATHI -> "कृपया या चित्रातील गृहपाठाचा प्रश्न सोडवून स्पष्ट करा."
        }

        // Clear input and attachment immediately for responsive UI
        _uiState.update {
            it.copy(
                inputText = "",
                attachedImageUri = null,
                attachedBitmap = null,
                isGenerating = true
            )
        }

        viewModelScope.launch {
            // Save user message
            repository.insertMessage(
                text = promptToSend,
                isUser = true,
                subject = currentState.selectedSubject.title,
                messageType = MessageType.CHAT,
                imageUri = attachedUriString
            )

            try {
                val response = tutorEngine.askTutor(
                    question = promptToSend,
                    subject = currentState.selectedSubject,
                    bitmap = attachedBitmap,
                    language = lang
                )

                repository.insertMessage(
                    text = response,
                    isUser = false,
                    subject = currentState.selectedSubject.title,
                    messageType = MessageType.CHAT
                )
            } catch (e: Exception) {
                val errorMsg = when (lang) {
                    AppLanguage.ENGLISH -> "⚠️ Sorry, I encountered an issue analyzing this question: ${e.message}. Please try again!"
                    AppLanguage.HINDI -> "⚠️ क्षमा करें, विश्लेषण में समस्या आई: ${e.message}। कृपया पुनः प्रयास करें!"
                    AppLanguage.MARATHI -> "⚠️ माफ करा, विश्लेषण करताना त्रुटी आली: ${e.message}. कृपया पुन्हा प्रयत्न करा!"
                }
                repository.insertMessage(
                    text = errorMsg,
                    isUser = false,
                    subject = currentState.selectedSubject.title,
                    messageType = MessageType.SYSTEM
                )
            } finally {
                _uiState.update { it.copy(isGenerating = false) }
            }
        }
    }

    fun openExplainDialog() {
        _uiState.update { it.copy(showExplainDialog = true) }
    }

    fun closeExplainDialog() {
        _uiState.update { it.copy(showExplainDialog = false) }
    }

    fun submitExplainTopic(topic: String, depthLevel: String) {
        if (topic.isBlank()) return
        closeExplainDialog()

        val currentState = _uiState.value
        val subject = currentState.selectedSubject
        val lang = currentState.selectedLanguage
        _uiState.update { it.copy(isGenerating = true) }

        viewModelScope.launch {
            val userPromptText = when (lang) {
                AppLanguage.ENGLISH -> "💡 **Explain Topic: $topic**\nLevel: $depthLevel"
                AppLanguage.HINDI -> "💡 **विषय स्पष्टीकरण: $topic**\nस्तर: $depthLevel"
                AppLanguage.MARATHI -> "💡 **संकल्पना स्पष्टीकरण: $topic**\nपातळी: $depthLevel"
            }

            repository.insertMessage(
                text = userPromptText,
                isUser = true,
                subject = subject.title,
                messageType = MessageType.EXPLANATION
            )

            try {
                val explanation = tutorEngine.explainTopic(topic, subject, depthLevel, lang)
                repository.insertMessage(
                    text = explanation,
                    isUser = false,
                    subject = subject.title,
                    messageType = MessageType.EXPLANATION
                )
            } catch (e: Exception) {
                repository.insertMessage(
                    text = "⚠️ Could not generate topic explanation: ${e.message}",
                    isUser = false,
                    subject = subject.title,
                    messageType = MessageType.SYSTEM
                )
            } finally {
                _uiState.update { it.copy(isGenerating = false) }
            }
        }
    }

    fun openMakeQuizDialog() {
        _uiState.update { it.copy(showMakeQuizDialog = true) }
    }

    fun closeMakeQuizDialog() {
        _uiState.update { it.copy(showMakeQuizDialog = false) }
    }

    fun submitMakeQuiz(customTopic: String? = null) {
        closeMakeQuizDialog()
        val currentState = _uiState.value
        val subject = currentState.selectedSubject
        val lang = currentState.selectedLanguage
        _uiState.update { it.copy(isGenerating = true) }

        val topicTitle = if (!customTopic.isNullOrBlank()) customTopic else subject.localizedTitle(lang)

        viewModelScope.launch {
            val userQuizText = when (lang) {
                AppLanguage.ENGLISH -> "📝 **Make Quiz:** Generate interactive quiz on '$topicTitle'"
                AppLanguage.HINDI -> "📝 **क्विज़ बनाएं:** '$topicTitle' पर इंटरैक्टिव क्विज़ तैयार करें"
                AppLanguage.MARATHI -> "📝 **क्विझ तयार करा:** '$topicTitle' विषयावर सराव क्विझ बनवा"
            }

            repository.insertMessage(
                text = userQuizText,
                isUser = true,
                subject = subject.title,
                messageType = MessageType.QUIZ
            )

            try {
                val quizData = tutorEngine.generateQuiz(subject, customTopic, lang)
                val quizBanner = when (lang) {
                    AppLanguage.ENGLISH -> "📝 **${quizData.subject} Quiz: ${quizData.topic}**\nTest your knowledge with these 3 questions!"
                    AppLanguage.HINDI -> "📝 **${quizData.subject} क्विज़: ${quizData.topic}**\nइन 3 प्रश्नों के साथ अपने ज्ञान का परीक्षण करें!"
                    AppLanguage.MARATHI -> "📝 **${quizData.subject} क्विझ: ${quizData.topic}**\nया 3 प्रश्नांसह स्वतःची तयारी तपासा!"
                }

                repository.insertMessage(
                    text = quizBanner,
                    isUser = false,
                    subject = subject.title,
                    messageType = MessageType.QUIZ,
                    quizData = quizData
                )
            } catch (e: Exception) {
                repository.insertMessage(
                    text = "⚠️ Could not generate quiz: ${e.message}",
                    isUser = false,
                    subject = subject.title,
                    messageType = MessageType.SYSTEM
                )
            } finally {
                _uiState.update { it.copy(isGenerating = false) }
            }
        }
    }

    fun onAnswerQuizOption(
        messageId: Long,
        questionIndex: Int,
        optionIndex: Int,
        currentQuiz: QuizData
    ) {
        viewModelScope.launch {
            repository.updateQuizAnswer(
                messageId = messageId,
                questionIndex = questionIndex,
                selectedOptionIndex = optionIndex,
                currentQuizData = currentQuiz
            )
        }
    }

    fun openClearConfirmDialog() {
        _uiState.update { it.copy(showClearConfirmDialog = true) }
    }

    fun closeClearConfirmDialog() {
        _uiState.update { it.copy(showClearConfirmDialog = false) }
    }

    fun confirmClearChat() {
        closeClearConfirmDialog()
        val lang = _uiState.value.selectedLanguage
        val clearedMsg = when (lang) {
            AppLanguage.ENGLISH -> "👋 Chat cleared! Pick a subject and ask me anything you'd like to study today."
            AppLanguage.HINDI -> "👋 चैट साफ कर दी गई है! कोई विषय चुनें और जो चाहें पूछें।"
            AppLanguage.MARATHI -> "👋 चॅट साफ झाली आहे! एक विषय निवडा आणि तुम्हाला हवा असलेला प्रश्न विचारा."
        }

        viewModelScope.launch {
            repository.clearAll()
            repository.insertMessage(
                text = clearedMsg,
                isUser = false,
                subject = _uiState.value.selectedSubject.title,
                messageType = MessageType.CHAT
            )
        }
    }

    fun dismissInfoMessage() {
        _uiState.update { it.copy(infoMessage = null) }
    }
}
