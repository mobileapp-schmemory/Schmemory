package site.jwojcik.schmemory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SpeechScreen(speechId: Int, onUpClick: () -> Boolean) {
    TODO("Not yet implemented")
}

@Preview
@Composable
fun SpeechPreview() {
    SpeechScreen(
        speechId = 0,
        onUpClick = {
            true
        },
    )
}