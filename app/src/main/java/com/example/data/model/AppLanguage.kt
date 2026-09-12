package com.example.data.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flag: String,
    val tagline: String,
    val inputPlaceholder: String,
    val sendButtonText: String,
    val makeQuizTitle: String,
    val explainTopicTitle: String,
    val clearChatTitle: String,
    val homeworkBannerTitle: String,
    val homeworkBannerSubtitle: String
) {
    ENGLISH(
        code = "en",
        displayName = "English",
        nativeName = "English",
        flag = "🇬🇧",
        tagline = "Your smart learning companion",
        inputPlaceholder = "Ask your study question or attach homework...",
        sendButtonText = "Send",
        makeQuizTitle = "📝 Make Quiz",
        explainTopicTitle = "💡 Explain Topic",
        clearChatTitle = "🗑️ Clear",
        homeworkBannerTitle = "📷 Homework Photo Attached",
        homeworkBannerSubtitle = "SUMIT AI will analyze this problem"
    ),
    HINDI(
        code = "hi",
        displayName = "Hindi",
        nativeName = "हिन्दी",
        flag = "🇮🇳",
        tagline = "आपका स्मार्ट स्टडी साथी",
        inputPlaceholder = "अपना सवाल पूछें या होमवर्क फोटो जोड़ें...",
        sendButtonText = "भेजें",
        makeQuizTitle = "📝 क्विज़ बनाएं",
        explainTopicTitle = "💡 विषय समझें",
        clearChatTitle = "🗑️ साफ करें",
        homeworkBannerTitle = "📷 होमवर्क फोटो जोड़ा गया",
        homeworkBannerSubtitle = "SUMIT AI इस समस्या का समाधान करेगा"
    ),
    MARATHI(
        code = "mr",
        displayName = "Marathi",
        nativeName = "मराठी",
        flag = "🚩",
        tagline = "तुमचा स्मार्ट अभ्यास मित्र",
        inputPlaceholder = "तुमचा अभ्यासाचा प्रश्न विचारा किंवा गृहपाठ जोडा...",
        sendButtonText = "पाठवा",
        makeQuizTitle = "📝 क्विझ बनवा",
        explainTopicTitle = "💡 विषय समजावा",
        clearChatTitle = "🗑️ साफ करा",
        homeworkBannerTitle = "📷 गृहपाठ फोटो जोडला",
        homeworkBannerSubtitle = "SUMIT AI या समस्येचे विश्लेषण करेल"
    );

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
