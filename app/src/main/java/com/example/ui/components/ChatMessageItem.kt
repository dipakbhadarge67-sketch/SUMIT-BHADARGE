package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatMessage
import com.example.data.model.MessageType
import com.example.data.model.QuizData
import com.example.ui.theme.StudyIndigo
import com.example.ui.theme.StudyIndigoLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onAnswerQuizOption: (messageId: Long, questionIndex: Int, optionIndex: Int, currentQuiz: QuizData) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timeFormatted = rememberTime(message.timestamp)

    if (message.messageType == MessageType.SYSTEM) {
        // System banner / Notice
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = StudyIndigo,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = message.text,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        return
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        if (!message.isUser) {
            // Bot Avatar
            Surface(
                shape = CircleShape,
                color = StudyIndigo.copy(alpha = 0.15f),
                modifier = Modifier
                    .size(36.dp)
                    .padding(top = 4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🤖", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 320.dp),
            horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
        ) {
            // User photo attachment preview if present
            if (message.imageUri != null) {
                AsyncImage(
                    model = message.imageUri,
                    contentDescription = "Attached homework photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .size(width = 220.dp, height = 150.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            // Embedded Quiz Card or Standard Bubble
            if (message.quizData != null) {
                InteractiveQuizCard(
                    quizData = message.quizData,
                    onAnswerSelected = { qIdx, optIdx ->
                        onAnswerQuizOption(message.id, qIdx, optIdx, message.quizData)
                    }
                )
            } else {
                Card(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (message.isUser) StudyIndigo else MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (message.isUser) 1.dp else 2.dp),
                    border = if (message.isUser) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                    modifier = Modifier.testTag(if (message.isUser) "user_msg_bubble" else "bot_msg_bubble")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = formatStudyText(message.text, isUser = message.isUser),
                            color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            lineHeight = 21.sp
                        )
                    }
                }
            }

            // Timestamp and copy button
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeFormatted,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )

                if (!message.isUser) {
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = {
                            copyToClipboard(context, message.text)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }

        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = StudyIndigoLight.copy(alpha = 0.2f),
                modifier = Modifier
                    .size(36.dp)
                    .padding(top = 4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "🎓", fontSize = 18.sp)
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("AI Study Buddy", text)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}

@Composable
private fun rememberTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

/**
 * Lightweight helper to format simple markdown elements:
 * **bold**, `code`, bullet points
 */
fun formatStudyText(rawText: String, isUser: Boolean): AnnotatedString {
    val builder = AnnotatedString.Builder()
    val parts = rawText.split("**")

    parts.forEachIndexed { index, part ->
        if (index % 2 == 1) {
            // Bold element
            builder.withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = if (isUser) Color.White else StudyIndigo
                )
            ) {
                append(part)
            }
        } else {
            // Handle code tags like `x = 5`
            val codeParts = part.split("`")
            codeParts.forEachIndexed { codeIdx, codePart ->
                if (codeIdx % 2 == 1) {
                    builder.withStyle(
                        style = SpanStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            background = if (isUser) Color.White.copy(alpha = 0.2f) else Color(0xFFF1F2F6)
                        )
                    ) {
                        append(" $codePart ")
                    }
                } else {
                    builder.append(codePart)
                }
            }
        }
    }
    return builder.toAnnotatedString()
}
