package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.SubjectCodeColor
import com.example.ui.theme.SubjectCurrentAffairsColor
import com.example.ui.theme.SubjectEconomicsColor
import com.example.ui.theme.SubjectEnglishColor
import com.example.ui.theme.SubjectGeographyColor
import com.example.ui.theme.SubjectHindiColor
import com.example.ui.theme.SubjectHistoryColor
import com.example.ui.theme.SubjectMarathiColor
import com.example.ui.theme.SubjectMathColor
import com.example.ui.theme.SubjectScienceColor

enum class Subject(
    val title: String,
    val icon: String,
    val color: Color,
    val starterQuestions: List<String>,
    val welcomeTip: String
) {
    MATH(
        title = "Math",
        icon = "📐",
        color = SubjectMathColor,
        starterQuestions = listOf(
            "Solve 3x + 7 = 22 step by step",
            "Explain Pythagorean Theorem",
            "How do quadratic equations work?",
            "What is a derivative in calculus?"
        ),
        welcomeTip = "Ask any calculation, algebra, geometry or calculus problem!"
    ),
    SCIENCE(
        title = "Science",
        icon = "🔬",
        color = SubjectScienceColor,
        starterQuestions = listOf(
            "Explain photosynthesis simply",
            "What are Newton's 3 laws?",
            "Difference between DNA and RNA",
            "How does gravity warp spacetime?"
        ),
        welcomeTip = "Explore physics, chemistry, biology, and astronomy with me!"
    ),
    GEOGRAPHY(
        title = "Geography",
        icon = "🌍",
        color = SubjectGeographyColor,
        starterQuestions = listOf(
            "How do monsoon winds form in India?",
            "Plate tectonics and earthquake zones",
            "Difference between weather and climate",
            "Major rivers of India and their origins"
        ),
        welcomeTip = "Explore physical geography, climate systems, maps, and landforms!"
    ),
    ECONOMICS(
        title = "Economics",
        icon = "📈",
        color = SubjectEconomicsColor,
        starterQuestions = listOf(
            "What is GDP and how is it measured?",
            "How does inflation affect purchasing power?",
            "Fiscal policy vs Monetary policy",
            "Role of the Reserve Bank of India (RBI)"
        ),
        welcomeTip = "Understand micro/macro economics, market trends, budget, and monetary policy!"
    ),
    CURRENT_AFFAIRS(
        title = "Current Affairs",
        icon = "📰",
        color = SubjectCurrentAffairsColor,
        starterQuestions = listOf(
            "Key national and global events this year",
            "Major international summits (G20, BRICS)",
            "Important government schemes and welfare programs",
            "Recent scientific breakthroughs and ISRO missions"
        ),
        welcomeTip = "Stay updated with current events, national affairs, awards, and summits!"
    ),
    MARATHI(
        title = "Marathi",
        icon = "🚩",
        color = SubjectMarathiColor,
        starterQuestions = listOf(
            "मराठीतील प्रमुख समास व त्यांची उदाहरणे",
            "विभक्ती प्रत्यय आणि त्यांचे कारकार्थ",
            "मराठी म्हणी व वाक्प्रचार अर्थासहित",
            "समानार्थी व विरुद्धार्थी शब्द सराव"
        ),
        welcomeTip = "Master Marathi grammar, literature, proverbs, and essay writing!"
    ),
    HINDI(
        title = "Hindi",
        icon = "🇮🇳",
        color = SubjectHindiColor,
        starterQuestions = listOf(
            "संधि और समास में क्या अंतर है?",
            "प्रमुख मुहावरे और उनके अर्थ",
            "अलंकार के भेद और उदाहरण",
            "संज्ञा, सर्वनाम और क्रिया के नियम"
        ),
        welcomeTip = "Practice Hindi grammar, idioms, sandhi, alankar, and composition!"
    ),
    ENGLISH(
        title = "English",
        icon = "📚",
        color = SubjectEnglishColor,
        starterQuestions = listOf(
            "Metaphor vs Simile examples",
            "How to write a strong thesis",
            "Active vs Passive voice rules",
            "Common literary devices"
        ),
        welcomeTip = "Ask grammar questions, essay outlines, or literature analysis!"
    ),
    HISTORY(
        title = "History",
        icon = "🏛️",
        color = SubjectHistoryColor,
        starterQuestions = listOf(
            "Key causes of World War I",
            "Why did the Roman Empire fall?",
            "Industrial Revolution impacts",
            "Timeline of the French Revolution"
        ),
        welcomeTip = "Deep dive into historical events, timelines, and world figures!"
    ),
    CODING(
        title = "Coding",
        icon = "💻",
        color = SubjectCodeColor,
        starterQuestions = listOf(
            "Explain recursion with an example",
            "What is Big-O notation?",
            "Array vs LinkedList differences",
            "How do REST APIs work?"
        ),
        welcomeTip = "Master programming logic, data structures, and debugging!"
    );

    fun localizedTitle(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> title
        AppLanguage.HINDI -> when (this) {
            MATH -> "गणित"
            SCIENCE -> "विज्ञान"
            GEOGRAPHY -> "भूगोल"
            ECONOMICS -> "अर्थशास्त्र"
            CURRENT_AFFAIRS -> "समसामयिकी"
            MARATHI -> "मराठी"
            HINDI -> "हिन्दी"
            ENGLISH -> "अंग्रेज़ी"
            HISTORY -> "इतिहास"
            CODING -> "कोडिंग"
        }
        AppLanguage.MARATHI -> when (this) {
            MATH -> "गणित"
            SCIENCE -> "विज्ञान"
            GEOGRAPHY -> "भूगोल"
            ECONOMICS -> "अर्थशास्त्र"
            CURRENT_AFFAIRS -> "चालू घडामोडी"
            MARATHI -> "मराठी"
            HINDI -> "हिंदी"
            ENGLISH -> "इंग्रजी"
            HISTORY -> "इतिहास"
            CODING -> "कोडिंग"
        }
    }

    fun localizedTip(language: AppLanguage): String = when (language) {
        AppLanguage.ENGLISH -> welcomeTip
        AppLanguage.HINDI -> when (this) {
            MATH -> "गणना, बीजगणित, ज्यामिति या कैलकुलस का कोई भी प्रश्न पूछें!"
            SCIENCE -> "भौतिकी, रसायन, जीवविज्ञान और खगोल विज्ञान के प्रश्न पूछें!"
            GEOGRAPHY -> "भौगोलिक संरचना, जलवायु, नदियां, मानचित्र और प्राकृतिक संसाधनों का अध्ययन करें!"
            ECONOMICS -> "अर्थव्यवस्था, GDP, मुद्रास्फीति, बजट और बैंकिंग प्रणाली को समझें!"
            CURRENT_AFFAIRS -> "राष्ट्रीय-अंतर्राष्ट्रीय घटनाओं, पुरस्कारों और सरकारी योजनाओं की तैयारी करें!"
            MARATHI -> "मराठी व्याकरण, म्हणी, वाक्प्रचार और निबंध लेखन सीखें!"
            HINDI -> "हिन्दी व्याकरण, मुहावरे, संधि, अलंकार और निबंध लेखन का अभ्यास करें!"
            ENGLISH -> "व्याकरण, निबंध रूपरेखा और साहित्यिक विश्लेषण के प्रश्न पूछें!"
            HISTORY -> "ऐतिहासिक घटनाओं, कालक्रम और विश्व हस्तियों के बारे में जानें!"
            CODING -> "प्रोग्रामिंग लॉजिक, डेटा संरचनाएं और डिबगिंग सीखें!"
        }
        AppLanguage.MARATHI -> when (this) {
            MATH -> "कोणतेही समीकरण, बीजगणित, भूमिती किंवा कॅल्क्युलस प्रश्न विचारा!"
            SCIENCE -> "भौतिकशास्त्र, रसायनशास्त्र आणि जीवशास्त्रातील शंका विचारा!"
            GEOGRAPHY -> "प्राकृतिक भूगोल, हवामान, नद्या, नकाशे आणि संसाधनांचा सविस्तर अभ्यास करा!"
            ECONOMICS -> "अर्थव्यवस्था, GDP, महागाई, अर्थसंकल्प आणि बँकिंग प्रणाली सोप्या भाषेत समजून घ्या!"
            CURRENT_AFFAIRS -> "चालू घडामोडी, राष्ट्रीय-आंतरराष्ट्रीय घटना, पुरस्कार आणि स्पर्धा परीक्षांची तयारी करा!"
            MARATHI -> "मराठी व्याकरण, साहित्य, म्हणी, वाक्प्रचार आणि निबंध लेखनाचा सराव करा!"
            HINDI -> "हिंदी व्याकरण, म्हणी, मुहावरे, अलंकार आणि साहित्य अभ्यास करा!"
            ENGLISH -> "इंग्रजी व्याकरण, निबंध लेखन आणि साहित्याबद्दल शंका विचारा!"
            HISTORY -> "ऐतिहासिक घटना, कालरेषा आणि महान व्यक्तींबद्दल जाणून घ्या!"
            CODING -> "प्रोग्रॅमिंग लॉजिक, डेटा स्ट्रक्चर्स आणि कोडिंग शिका!"
        }
    }

    fun localizedStarterQuestions(language: AppLanguage): List<String> = when (language) {
        AppLanguage.ENGLISH -> starterQuestions
        AppLanguage.HINDI -> when (this) {
            MATH -> listOf(
                "3x + 7 = 22 को चरण-दर-चरण हल करें",
                "पाइथागोरस प्रमेय समझाइए",
                "द्विघात समीकरण कैसे हल करें?",
                "कैलकुलस में अवकलज (Derivative) क्या है?"
            )
            SCIENCE -> listOf(
                "प्रकाश संश्लेषण (Photosynthesis) समझाइए",
                "न्यूटन के 3 नियम क्या हैं?",
                "DNA और RNA में क्या अंतर है?",
                "गुरुत्वाकर्षण कैसे काम करता है?"
            )
            GEOGRAPHY -> listOf(
                "भारत में मानसून की उत्पत्ति कैसे होती है?",
                "प्लेट विवर्तनिकी और भूकंप क्षेत्र",
                "मौसम और जलवायु में अंतर",
                "भारत की प्रमुख नदियां और उनके उद्गम"
            )
            ECONOMICS -> listOf(
                "GDP क्या है और इसकी गणना कैसे होती है?",
                "मुद्रास्फीति (महंगाई) का क्या असर होता है?",
                "राजकोषीय और मौद्रिक नीति में अंतर",
                "भारतीय रिजर्व बैंक (RBI) के प्रमुख कार्य"
            )
            CURRENT_AFFAIRS -> listOf(
                "इस वर्ष की प्रमुख राष्ट्रीय और अंतर्राष्ट्रीय घटनाएं",
                "प्रमुख अंतर्राष्ट्रीय शिखर सम्मेलन (G20, BRICS)",
                "महत्वपूर्ण सरकारी योजनाएं और नीतियां",
                "ISRO और अंतरिक्ष अनुसंधान के नवीनतम मिशन"
            )
            MARATHI -> listOf(
                "मराठीतील प्रमुख समास व त्यांची उदाहरणे",
                "विभक्ती प्रत्यय आणि त्यांचे कारकार्थ",
                "मराठी म्हणी व वाक्प्रचार अर्थासहित",
                "समानार्थी व विरुद्धार्थी शब्द सराव"
            )
            HINDI -> listOf(
                "संधि और समास में क्या अंतर है?",
                "प्रमुख मुहावरे और उनके अर्थ",
                "अलंकार के भेद और उदाहरण",
                "संज्ञा, सर्वनाम और क्रिया के नियम"
            )
            ENGLISH -> listOf(
                "Metaphor और Simile के उदाहरण",
                "अच्छा निबंध (Thesis) कैसे लिखें?",
                "Active vs Passive Voice के नियम",
                "Tenses आसानी से समझें"
            )
            HISTORY -> listOf(
                "प्रथम विश्व युद्ध के मुख्य कारण",
                "सिंधु घाटी सभ्यता की विशेषताएं",
                "औद्योगिक क्रांति के प्रभाव",
                "भारतीय स्वतंत्रता संग्राम का इतिहास"
            )
            CODING -> listOf(
                "Recursion को सरल उदाहरण से समझाएं",
                "Big-O Notation क्या है?",
                "Array और LinkedList में अंतर",
                "REST API कैसे काम करती है?"
            )
        }
        AppLanguage.MARATHI -> when (this) {
            MATH -> listOf(
                "3x + 7 = 22 टप्प्याटप्प्याने सोडवा",
                "पायथागोरसचा सिद्धांत समजावून सांगा",
                "वर्गसमीकरणे कशी सोडवावी?",
                "कॅल्क्युलसमध्ये डेरिव्हेटिव्ह काय आहे?"
            )
            SCIENCE -> listOf(
                "प्रकाशसंश्लेषण सोप्या भाषेत स्पष्ट करा",
                "न्यूटनचे गतीचे ३ नियम सांगा",
                "DNA आणि RNA मधील फरक",
                "गुरुत्वाकर्षण शक्ती कशी कार्य करते?"
            )
            GEOGRAPHY -> listOf(
                "भारतात मान्सून वारे कसे निर्माण होतात?",
                "भूकंप प्रवण क्षेत्र व भूकवच हालचाली",
                "हवामान आणि हवा यातील फरक",
                "महाराष्ट्रातील व भारतातील प्रमुख नद्या"
            )
            ECONOMICS -> listOf(
                "GDP म्हणजे काय आणि तो कसा मोजला जातो?",
                "महागाईचा (Inflation) सर्वसामान्यांवर काय परिणाम होतो?",
                "राजकोषीय धोरण आणि चलनविषयक धोरण यातील फरक",
                "रिझर्व्ह बँक ऑफ इंडिया (RBI) चे प्रमुख कार्य"
            )
            CURRENT_AFFAIRS -> listOf(
                "चालू वर्षातील महत्त्वाच्या राष्ट्रीय व जागतिक घडामोडी",
                "प्रमुख आंतरराष्ट्रीय परिषदा (G20, BRICS)",
                "शासनाच्या महत्त्वाच्या कल्याणकारी योजना",
                "ISRO व अवकाश विज्ञानातील नवीन मोहिमा"
            )
            MARATHI -> listOf(
                "मराठीतील प्रमुख समास व त्यांची उदाहरणे",
                "विभक्ती प्रत्यय आणि त्यांचे कारकार्थ",
                "मराठी म्हणी व वाक्प्रचार अर्थासहित",
                "समानार्थी व विरुद्धार्थी शब्द सराव"
            )
            HINDI -> listOf(
                "संधी आणि समास यातील मुख्य फरक",
                "हिंदीतील महत्त्वाचे म्हणी व मुहावरे",
                "अलंकार व त्यांचे प्रकार उदाहरणासह",
                "हिंदी व्याकरण व निबंध लेखन"
            )
            ENGLISH -> listOf(
                "Metaphor आणि Simile मधील फरक",
                "उत्तम निबंध कसा लिहावा?",
                "Active आणि Passive Voice चे नियम",
                "इंग्रजी व्याकरणाचे सोपे नियम"
            )
            HISTORY -> listOf(
                "पहिल्या महायुद्धाची प्रमुख कारणे",
                "छत्रपती शिवाजी महाराजांची युद्धनीती",
                "औद्योगिक क्रांतीचे परिणाम",
                "भारतीय स्वातंत्र्य लढा माहिती"
            )
            CODING -> listOf(
                "Recursion सोप्या उदाहरणाने समजावा",
                "Big-O Notation म्हणजे काय?",
                "Array आणि LinkedList मधील फरक",
                "REST API कशी कार्य करते?"
            )
        }
    }

    companion object {
        fun fromTitle(title: String): Subject {
            return entries.firstOrNull { subject ->
                subject.title.equals(title, ignoreCase = true) ||
                        subject.name.equals(title, ignoreCase = true) ||
                        subject.localizedTitle(AppLanguage.HINDI).equals(title, ignoreCase = true) ||
                        subject.localizedTitle(AppLanguage.MARATHI).equals(title, ignoreCase = true) ||
                        subject.localizedTitle(AppLanguage.ENGLISH).equals(title, ignoreCase = true)
            } ?: MATH
        }
    }
}
