package site.jwojcik.schmemory.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    onScenesClick: () -> Unit,
    onSpeechesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Text("Home Screen")
}