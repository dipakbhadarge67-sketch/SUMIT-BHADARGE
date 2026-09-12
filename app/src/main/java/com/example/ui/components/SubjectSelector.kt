package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun SubjectSelector(
    selectedSubject: Subject,
    onSubjectSelected: (Subject) -> Unit,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("subject_selector"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Subject.entries.forEach { subject ->
            val isSelected = subject == selectedSubject
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) StudyIndigo else MaterialTheme.colorScheme.surfaceVariant,
                label = "subject_bg"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "subject_color"
            )

            Surface(
                onClick = { onSubjectSelected(subject) },
                shape = RoundedCornerShape(14.dp),
                color = backgroundColor,
                shadowElevation = if (isSelected) 3.dp else 0.dp,
                modifier = Modifier.testTag("subject_btn_${subject.name.lowercase()}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subject.icon,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = subject.localizedTitle(selectedLanguage),
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
