package site.jwojcik.schmemory.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import site.jwojcik.schmemory.Routes
import site.jwojcik.schmemory.SchmemoryApplication
import site.jwojcik.schmemory.data.Scene
import site.jwojcik.schmemory.data.SceneLine
import site.jwojcik.schmemory.data.SchmemoryRepository

class SceneViewModel(
    savedStateHandle: SavedStateHandle,
    private val schmRepo: SchmemoryRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SchmemoryApplication)
                SceneViewModel(this.createSavedStateHandle(), application.schmemoryRepository)
            }
        }
    }

    private val sceneId: Long = savedStateHandle.toRoute<Routes.Scene>().sceneId

    private val currLineNum = MutableStateFlow(1)
    private val answerVisible = MutableStateFlow(false)

    val uiState: StateFlow<SceneLineScreenUiState> =
        combine(
            schmRepo.getScene(sceneId).filterNotNull(),
            schmRepo.getSceneLines(sceneId),
            currLineNum,
            answerVisible
        ) { scene, lines, currNum, ansVisible ->
            val sortedLines = lines.sortedBy { it.order }
            val currentLineIndex = (currNum - 1).coerceIn(0, (sortedLines.size - 1).coerceAtLeast(0))
            val currentLine = if (sortedLines.isNotEmpty()) sortedLines[currentLineIndex] else null
            
            // Filter for lines BEFORE the current line that belong to other characters
            // and lines for the current character that we've already passed? 
            // Actually, the request says "present the lines before the character's line who's being read for"
            // Usually this means showing the context (cues) before the user's line.
            
            val previousLines = if (currentLine != null) {
                sortedLines.take(currentLineIndex)
            } else {
                emptyList()
            }

            SceneLineScreenUiState(
                scene = scene,
                lineList = sortedLines,
                currSceneLine = currentLine ?: SceneLine(0, 0, 0, "", ""),
                previousLines = previousLines,
                currSceneLineNum = currNum,
                totalSceneLines = sortedLines.size,
                answerVisible = ansVisible
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = SceneLineScreenUiState(
                    scene = Scene(0, "", ""),
                    currSceneLine = SceneLine(0, 0, 0, "", "")
                )
            )

    fun prevSceneLine() {
        val total = uiState.value.totalSceneLines
        if (total == 0) return
        currLineNum.value = if (currLineNum.value > 1) currLineNum.value - 1 else total
        answerVisible.value = false
    }

    fun nextSceneLine() {
        val total = uiState.value.totalSceneLines
        if (total == 0) return
        currLineNum.value = if (currLineNum.value < total) currLineNum.value + 1 else 1
        answerVisible.value = false
    }

    fun toggleAnswer() {
        answerVisible.value = !answerVisible.value
    }

    fun deleteSceneLine() {
        // Implementation for deleting the current line if needed
    }
}

data class SceneLineScreenUiState(
    val scene: Scene,
    val currSceneLine: SceneLine,
    val lineList: List<SceneLine> = emptyList(),
    val previousLines: List<SceneLine> = emptyList(),
    val currSceneLineNum: Int = 1,
    val totalSceneLines: Int = 0,
    val answerVisible: Boolean = false
)
