package site.jwojcik.schmemory.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(
    onScenesClick: () -> Unit,
    onSpeechesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Text("Home Screen")
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(
        onScenesClick = {},
        onSpeechesClick = {},
        onSettingsClick = {}
    )
}