package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.StudyIndigo

@Composable
fun LanguageSelectorDialog(
    currentLanguage: AppLanguage,
    canDismiss: Boolean = true,
    onDismiss: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    var selectedLang by remember { mutableStateOf(currentLanguage) }

    AlertDialog(
        onDismissRequest = {
            if (canDismiss) onDismiss()
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = StudyIndigo.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = StudyIndigo,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Choose Language",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "भाषा चुनें / भाषा निवडा",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Select your preferred language for learning and chatting with SUMIT AI:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                AppLanguage.entries.forEach { lang ->
                    val isSelected = lang == selectedLang
                    val subtitle = when (lang) {
                        AppLanguage.ENGLISH -> "Learn in English"
                        AppLanguage.HINDI -> "हिंदी में पढ़ाई और सवाल-जवाब करें"
                        AppLanguage.MARATHI -> "मराठीतून अभ्यास आणि प्रश्नोत्तरे करा"
                    }

                    Surface(
                        onClick = { selectedLang = lang },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) StudyIndigo.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) StudyIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("lang_option_${lang.code}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lang.flag,
                                fontSize = 24.sp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = lang.nativeName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (isSelected) StudyIndigo else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (lang != AppLanguage.ENGLISH) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "(${lang.displayName})",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Text(
                                    text = subtitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = StudyIndigo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLanguageSelected(selectedLang)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyIndigo),
                modifier = Modifier.testTag("confirm_language_btn")
            ) {
                val btnText = when (selectedLang) {
                    AppLanguage.ENGLISH -> "Continue"
                    AppLanguage.HINDI -> "आगे बढ़ें"
                    AppLanguage.MARATHI -> "पुढे चला"
                }
                Text(btnText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            if (canDismiss) {
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val cancelText = when (selectedLang) {
                        AppLanguage.ENGLISH -> "Cancel"
                        AppLanguage.HINDI -> "रद्द करें"
                        AppLanguage.MARATHI -> "रद्द करा"
                    }
                    Text(cancelText)
                }
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
