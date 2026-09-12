package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizData
import com.example.ui.theme.StudyIndigo

@Composable
fun InteractiveQuizCard(
    quizData: QuizData,
    onAnswerSelected: (questionIndex: Int, optionIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_quiz_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header
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
                            imageVector = Icons.Default.Quiz,
                            contentDescription = "Quiz",
                            tint = StudyIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "📝 ${quizData.topic} Quiz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Subject: ${quizData.subject} • ${quizData.questions.size} Questions",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Questions list
            quizData.questions.forEachIndexed { qIdx, question ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "${qIdx + 1}. ${question.question}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Options
                    val optionLetters = listOf("A", "B", "C", "D")
                    question.options.forEachIndexed { optIdx, optionText ->
                        val isAnswered = question.userSelectedIndex != null
                        val isSelected = question.userSelectedIndex == optIdx
                        val isCorrect = question.correctIndex == optIdx

                        val containerColor = when {
                            !isAnswered -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            isSelected && isCorrect -> Color(0xFFD1FAE5) // light green
                            isSelected && !isCorrect -> Color(0xFFFEE2E2) // light red
                            isCorrect -> Color(0xFFE6F4EA) // subtle green hint
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        }

                        val borderColor = when {
                            !isAnswered -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            isSelected && isCorrect -> Color(0xFF10B981)
                            isSelected && !isCorrect -> Color(0xFFEF4444)
                            isCorrect -> Color(0xFF10B981).copy(alpha = 0.6f)
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        }

                        Surface(
                            onClick = {
                                if (!isAnswered) {
                                    onAnswerSelected(qIdx, optIdx)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = containerColor,
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .testTag("quiz_opt_${qIdx}_$optIdx")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) StudyIndigo else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = optionLetters.getOrElse(optIdx) { "?" },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = optionText,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isAnswered) {
                                    if (isSelected && isCorrect) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Correct",
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    } else if (isSelected && !isCorrect) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Incorrect",
                                            tint = Color(0xFFEF4444),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Box if answered
                    AnimatedVisibility(
                        visible = question.userSelectedIndex != null,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (question.userSelectedIndex == question.correctIndex)
                                Color(0xFFECFDF5) else Color(0xFFFFFBEB),
                            border = BorderStroke(
                                1.dp,
                                if (question.userSelectedIndex == question.correctIndex)
                                    Color(0xFF10B981).copy(alpha = 0.3f)
                                else
                                    Color(0xFFF59E0B).copy(alpha = 0.3f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Explanation",
                                    tint = if (question.userSelectedIndex == question.correctIndex)
                                        Color(0xFF059669) else Color(0xFFD97706),
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = question.explanation,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Quiz score tally if all answered
            val totalAnswered = quizData.questions.count { it.userSelectedIndex != null }
            val totalCorrect = quizData.questions.count { it.userSelectedIndex == it.correctIndex }

            if (totalAnswered > 0) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = StudyIndigo.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Score: $totalCorrect / ${quizData.questions.size} Correct",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = StudyIndigo
                        )
                        val pct = if (quizData.questions.isNotEmpty()) (totalCorrect * 100) / quizData.questions.size else 0
                        Text(
                            text = if (totalAnswered == quizData.questions.size) "$pct% Completed 🎉" else "$totalAnswered/${quizData.questions.size} Answered",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = StudyIndigo
                        )
                    }
                }
            }
        }
    }
}
