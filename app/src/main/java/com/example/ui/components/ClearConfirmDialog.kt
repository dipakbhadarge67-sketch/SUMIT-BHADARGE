package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage

@Composable
fun ClearConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH
) {
    val titleText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Clear Study Chat?"
        AppLanguage.HINDI -> "चैट साफ करें?"
        AppLanguage.MARATHI -> "अभ्यास चॅट साफ करावी?"
    }

    val bodyText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "This will clear your current conversation history. You can start fresh with any subject!"
        AppLanguage.HINDI -> "यह आपकी वर्तमान बातचीत को मिटा देगा। आप नए सिरे से किसी भी विषय पर शुरू कर सकते हैं!"
        AppLanguage.MARATHI -> "यामुळे तुमचा चालू संभाषण इतिहास नष्ट होईल. तुम्ही कोणत्याही विषयावर नव्याने सुरुवात करू शकता!"
    }

    val confirmText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Clear All 🗑️"
        AppLanguage.HINDI -> "सब साफ करें 🗑️"
        AppLanguage.MARATHI -> "सर्व साफ करा 🗑️"
    }

    val cancelText = when (selectedLanguage) {
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.HINDI -> "रद्द करें"
        AppLanguage.MARATHI -> "रद्द करा"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = null,
                tint = Color(0xFFEF4444)
            )
        },
        title = {
            Text(
                text = titleText,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(
                text = bodyText,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                modifier = Modifier.testTag("confirm_clear_btn")
            ) {
                Text(confirmText, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(cancelText)
            }
        }
    )
}
