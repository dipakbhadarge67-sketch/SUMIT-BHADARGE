package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = StudyIndigoDarkTheme,
    onPrimary = Color(0xFF1F1162),
    primaryContainer = StudyIndigoDark,
    onPrimaryContainer = Color(0xFFE2DDFF),
    secondary = StudyPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF422E68),
    onSecondaryContainer = Color(0xFFF1EBFF),
    tertiary = StudyTertiary,
    background = StudyBackgroundDark,
    surface = StudySurfaceDark,
    surfaceVariant = StudySurfaceVariantDark,
    onBackground = StudyOnSurfaceDark,
    onSurface = StudyOnSurfaceDark,
    outline = StudyOutlineDark
  )

private val LightColorScheme =
  lightColorScheme(
    primary = StudyIndigo,
    onPrimary = Color.White,
    primaryContainer = StudyPrimaryContainer,
    onPrimaryContainer = StudyOnPrimaryContainer,
    secondary = StudySecondary,
    onSecondary = Color.White,
    secondaryContainer = StudySecondaryContainer,
    onSecondaryContainer = StudyOnSecondaryContainer,
    tertiary = StudyTertiary,
    onTertiary = Color.White,
    tertiaryContainer = StudyTertiaryContainer,
    onTertiaryContainer = StudyOnTertiaryContainer,
    background = StudyBackgroundLight,
    surface = StudySurfaceLight,
    surfaceVariant = StudySurfaceVariantLight,
    onBackground = StudyOnSurfaceLight,
    onSurface = StudyOnSurfaceLight,
    outline = StudyOutlineLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use intentional branded palette
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

