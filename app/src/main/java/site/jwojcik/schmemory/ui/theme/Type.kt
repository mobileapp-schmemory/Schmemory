package site.jwojcik.schmemory.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import site.jwojcik.schmemory.R

val HappyMonkey = FontFamily(
    Font(R.font.happymonkey_regular)
)

private val defaultTypography = Typography()

private fun TextStyle.scaled(scale: Float, fontFamily: FontFamily): TextStyle {
    return this.copy(
        fontFamily = fontFamily,
        fontSize = this.fontSize * scale,
        lineHeight = this.lineHeight * scale
    )
}

/**
 * Returns the application typography with the specified [fontFamily] and [scale] factor applied.
 */
fun getTypography(fontFamily: FontFamily, scale: Float = 1f): Typography {
    return Typography(
        displayLarge = defaultTypography.displayLarge.scaled(scale, fontFamily),
        displayMedium = defaultTypography.displayMedium.scaled(scale, fontFamily),
        displaySmall = defaultTypography.displaySmall.scaled(scale, fontFamily),
        headlineLarge = defaultTypography.headlineLarge.scaled(scale, fontFamily),
        headlineMedium = defaultTypography.headlineMedium.scaled(scale, fontFamily),
        headlineSmall = defaultTypography.headlineSmall.scaled(scale, fontFamily),
        titleLarge = defaultTypography.titleLarge.scaled(scale, fontFamily),
        titleMedium = defaultTypography.titleMedium.scaled(scale, fontFamily),
        titleSmall = defaultTypography.titleSmall.scaled(scale, fontFamily),
        bodyLarge = defaultTypography.bodyLarge.scaled(scale, fontFamily),
        bodyMedium = defaultTypography.bodyMedium.scaled(scale, fontFamily),
        bodySmall = defaultTypography.bodySmall.scaled(scale, fontFamily),
        labelLarge = defaultTypography.labelLarge.scaled(scale, fontFamily),
        labelMedium = defaultTypography.labelMedium.scaled(scale, fontFamily),
        labelSmall = defaultTypography.labelSmall.scaled(scale, fontFamily)
    )
}

// Default typography using Happy Monkey at normal scale
val Typography = getTypography(HappyMonkey)
