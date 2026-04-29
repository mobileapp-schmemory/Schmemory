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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
            
            val isUserLine = currentLine?.characterName?.equals(scene.readingFor, ignoreCase = true) ?: false

            SceneLineScreenUiState(
                scene = scene,
                lineList = sortedLines,
                currSceneLine = currentLine ?: SceneLine(0, 0, 0, "", ""),
                previousLines = if (currentLine != null) sortedLines.take(currentLineIndex) else emptyList(),
                currSceneLineNum = currNum,
                totalSceneLines = sortedLines.size,
                answerVisible = ansVisible,
                isUserLine = isUserLine
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

    init {
        viewModelScope.launch {
            val scene = schmRepo.getScene(sceneId).filterNotNull().first()
            val lines = schmRepo.getSceneLines(sceneId).first()
            if (lines.isNotEmpty()) {
                val sortedLines = lines.sortedBy { it.order }
                val firstUserIndex = sortedLines.indexOfFirst {
                    it.characterName.equals(scene.readingFor, ignoreCase = true)
                }
                if (firstUserIndex != -1) {
                    currLineNum.value = firstUserIndex + 1
                }
            }
        }
    }

    fun prevSceneLine() {
        val state = uiState.value
        val lines = state.lineList
        val readingFor = state.scene.readingFor
        if (lines.isEmpty()) return
        
        val currentIndex = state.currSceneLineNum - 1
        
        if (readingFor.isBlank()) {
            currLineNum.value = if (currLineNum.value > 1) currLineNum.value - 1 else lines.size
            answerVisible.value = false
            return
        }

        var prevIndex = (currentIndex - 1 + lines.size) % lines.size
        var found = false
        val start = prevIndex
        
        do {
            if (lines[prevIndex].characterName.equals(readingFor, ignoreCase = true)) {
                found = true
                break
            }
            prevIndex = (prevIndex - 1 + lines.size) % lines.size
        } while (prevIndex != start)

        if (found) {
            currLineNum.value = prevIndex + 1
            answerVisible.value = false
        } else {
            currLineNum.value = if (currLineNum.value > 1) currLineNum.value - 1 else lines.size
            answerVisible.value = false
        }
    }

    fun nextSceneLine() {
        val state = uiState.value
        val lines = state.lineList
        val readingFor = state.scene.readingFor
        if (lines.isEmpty()) return
        
        val currentIndex = state.currSceneLineNum - 1
        
        if (readingFor.isBlank()) {
            currLineNum.value = if (currLineNum.value < lines.size) currLineNum.value + 1 else 1
            answerVisible.value = false
            return
        }
        
        var nextIndex = (currentIndex + 1) % lines.size
        var found = false
        val start = nextIndex
        
        do {
            if (lines[nextIndex].characterName.equals(readingFor, ignoreCase = true)) {
                found = true
                break
            }
            nextIndex = (nextIndex + 1) % lines.size
        } while (nextIndex != start)

        if (found) {
            currLineNum.value = nextIndex + 1
            answerVisible.value = false
        } else {
            currLineNum.value = if (currLineNum.value < lines.size) currLineNum.value + 1 else 1
            answerVisible.value = false
        }
    }

    fun toggleAnswer() {
        answerVisible.value = !answerVisible.value
    }

    fun deleteSceneLine() {
        // TODO: Complete this function
    }
}

data class SceneLineScreenUiState(
    val scene: Scene,
    val currSceneLine: SceneLine,
    val lineList: List<SceneLine> = emptyList(),
    val previousLines: List<SceneLine> = emptyList(),
    val currSceneLineNum: Int = 1,
    val totalSceneLines: Int = 0,
    val answerVisible: Boolean = false,
    val isUserLine: Boolean = false
)
