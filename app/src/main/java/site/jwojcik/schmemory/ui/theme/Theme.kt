package site.jwojcik.schmemory.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density

private val DarkColorScheme = darkColorScheme(
    primary = Blue,
    secondary = Green,
    background = Gray,
    surface = Gray,
    onPrimary = White,
    onBackground = White,
    surfaceVariant = Yellow
)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = Green,
    background = Yellow,
    surface = Yellow,
    onPrimary = Black,
    onBackground = Black,
    surfaceVariant = White
)

@Composable
fun SchmemoryTheme(
    darkTheme: Boolean,
    fontScale: Float,
    dyslexicFont: Boolean,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val currentTypography = if (dyslexicFont) {
        getTypography(FontFamily.Default)
    } else {
        Typography
    }

    // Apply the custom font scale by providing a modified Density
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = density.density,
            fontScale = density.fontScale * fontScale
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = currentTypography,
            content = content
        )
    }
}
