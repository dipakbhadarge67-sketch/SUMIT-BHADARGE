package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Subject
import com.example.ui.theme.StudyIndigo

@Composable
fun ExplainTopicDialog(
    currentSubject: Subject,
    onDismiss: () -> Unit,
    onSubmit: (topic: String, depthLevel: String) -> Unit,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH
) {
    var topicText by remember { mutableStateOf("") }
    val depthOptions = listOf("Like I'm 10", "High School", "Deep Dive", "Exam Bullets")
    var selectedDepth by remember { mutableStateOf("High School") }

    val dialogTitle = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "💡 Explain Any Topic"
        AppLanguage.HINDI -> "💡 कोई भी विषय समझें"
        AppLanguage.MARATHI -> "💡 कोणताही विषय समजावा"
    }

    val dialogSubtitle = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Enter a concept from ${currentSubject.title} or any field, and SUMIT AI will break it down intuitively."
        AppLanguage.HINDI -> "${currentSubject.localizedTitle(selectedLanguage)} या किसी भी क्षेत्र की कोई अवधारणा लिखें, SUMIT AI इसे सरलता से समझाएगा।"
        AppLanguage.MARATHI -> "${currentSubject.localizedTitle(selectedLanguage)} किंवा कोणत्याही क्षेत्रातील संकल्पना लिहा, SUMIT AI ती सोप्या भाषेत समजावून सांगेल."
    }

    val placeholderText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "e.g. Photosynthesis, Relativity, Metaphor..."
        AppLanguage.HINDI -> "जैसे: प्रकाश संश्लेषण, सापेक्षता, रूपक..."
        AppLanguage.MARATHI -> "उदा: प्रकाशसंश्लेषण, सापेक्षता, रूपक..."
    }

    val depthLabel = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Explanation Depth:"
        AppLanguage.HINDI -> "स्पष्टीकरण का स्तर:"
        AppLanguage.MARATHI -> "स्पष्टीकरणाची पातळी:"
    }

    val explainBtnText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Explain 💡"
        AppLanguage.HINDI -> "समझाएं 💡"
        AppLanguage.MARATHI -> "समजावा 💡"
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
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
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
                        .testTag("explain_topic_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = depthLabel,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    depthOptions.take(2).forEach { depth ->
                        val isSelected = selectedDepth == depth
                        Surface(
                            onClick = { selectedDepth = depth },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) StudyIndigo else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) StudyIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = depth,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    depthOptions.drop(2).forEach { depth ->
                        val isSelected = selectedDepth == depth
                        Surface(
                            onClick = { selectedDepth = depth },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) StudyIndigo else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) StudyIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = depth,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (topicText.isNotBlank()) {
                        onSubmit(topicText.trim(), selectedDepth)
                    }
                },
                enabled = topicText.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyIndigo),
                modifier = Modifier.testTag("submit_explain_btn")
            ) {
                Text(explainBtnText, fontWeight = FontWeight.Bold)
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
