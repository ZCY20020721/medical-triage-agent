package com.yidao.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    error = RedAlert,
    background = BackgroundLight,
    onBackground = OnBackground,
)

private val DarkColors = darkColorScheme(
    primary = BlueSecondary,
    secondary = BluePrimary,
    error = RedAlert,
)

@Composable
fun YidaoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content,
    )
}
