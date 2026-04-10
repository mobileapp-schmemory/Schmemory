package site.jwojcik.schmemory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

enum class SchmemoryListType { SCENE, SPEECH }

@Composable
fun ListScreen(listType: SchmemoryListType, onUpClick: () -> Boolean, onItemClick: (Int) -> Unit) {
    TODO("Not yet implemented")
}
