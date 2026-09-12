package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Subject
import com.example.ui.theme.StudyIndigo

@Composable
fun MakeQuizDialog(
    currentSubject: Subject,
    onDismiss: () -> Unit,
    onSubmit: (customTopic: String?) -> Unit,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH
) {
    var topicText by remember { mutableStateOf("") }

    val dialogTitle = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "📝 Make Practice Quiz"
        AppLanguage.HINDI -> "📝 अभ्यास क्विज़ बनाएं"
        AppLanguage.MARATHI -> "📝 सराव क्विझ बनवा"
    }

    val dialogSubtitle = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Generate an interactive 3-question quiz for ${currentSubject.icon} ${currentSubject.title}."
        AppLanguage.HINDI -> "${currentSubject.icon} ${currentSubject.localizedTitle(selectedLanguage)} के लिए 3 प्रश्नों का क्विज़ बनाएं।"
        AppLanguage.MARATHI -> "${currentSubject.icon} ${currentSubject.localizedTitle(selectedLanguage)} साठी ३ प्रश्नांची परस्परसंवादी क्विझ तयार करा."
    }

    val topicLabel = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Specific Topic (Optional):"
        AppLanguage.HINDI -> "विशेष विषय (वैकल्पिक):"
        AppLanguage.MARATHI -> "विशिष्ट विषय (पर्यायी):"
    }

    val placeholderText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Leave blank for general ${currentSubject.title}"
        AppLanguage.HINDI -> "सामान्य ${currentSubject.localizedTitle(selectedLanguage)} के लिए खाली छोड़ें"
        AppLanguage.MARATHI -> "सामान्य ${currentSubject.localizedTitle(selectedLanguage)} साठी रिक्त ठेवा"
    }

    val generateBtnText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Generate Quiz 📝"
        AppLanguage.HINDI -> "क्विज़ बनाएं 📝"
        AppLanguage.MARATHI -> "क्विझ तयार करा 📝"
    }

    val cancelBtnText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.HINDI -> "रद्द करें"
        AppLanguage.MARATHI -> "रद्द करा"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    tint = StudyIndigo,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dialogTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = dialogSubtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = topicLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = topicText,
                    onValueChange = { topicText = it },
                    placeholder = { Text(placeholderText) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StudyIndigo
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quiz_topic_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(topicText.trim().ifEmpty { null })
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyIndigo),
                modifier = Modifier.testTag("submit_quiz_btn")
            ) {
                Text(generateBtnText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(cancelBtnText)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
