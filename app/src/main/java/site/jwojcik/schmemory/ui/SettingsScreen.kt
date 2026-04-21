package site.jwojcik.schmemory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(onUpClick: () -> Boolean) {
    //TODO("Not yet implemented")
}

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        onUpClick = {
            true
        },
    )
}