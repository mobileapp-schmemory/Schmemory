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
            val currentLineIndex = currNum - 1
            val currentLine = if (sortedLines.isNotEmpty() && currentLineIndex < sortedLines.size) sortedLines[currentLineIndex] else null
            
            val isUserLine = currentLine?.characterName?.equals(scene.readingFor, ignoreCase = true) ?: false
            
            // Auto-reveal if not user line
            val effectiveAnsVisible = if (!isUserLine && currentLine != null) true else ansVisible

            // isAtEnd means we are on the last line. 
            // The Refresh icon should be shown here because there's no "Next" line.
            val isAtEnd = sortedLines.isNotEmpty() && currNum == sortedLines.size

            SceneLineScreenUiState(
                scene = scene,
                lineList = sortedLines,
                currSceneLine = currentLine ?: SceneLine(0, 0, 0, "", ""),
                previousLines = sortedLines.take(currentLineIndex.coerceIn(0, sortedLines.size)),
                currSceneLineNum = currNum.coerceAtMost(sortedLines.size),
                totalSceneLines = sortedLines.size,
                answerVisible = effectiveAnsVisible,
                isUserLine = isUserLine,
                isAtEnd = isAtEnd
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
        
        val currentIndex = currLineNum.value - 1
        
        if (readingFor.isBlank()) {
            currLineNum.value = if (currLineNum.value > 1) currLineNum.value - 1 else 1
            answerVisible.value = false
            return
        }

        var prevUserIndex = -1
        for (i in (currentIndex - 1) downTo 0) {
            if (lines[i].characterName.equals(readingFor, ignoreCase = true)) {
                prevUserIndex = i
                break
            }
        }

        if (prevUserIndex != -1) {
            currLineNum.value = prevUserIndex + 1
        } else {
            val firstUserIndex = lines.indexOfFirst { it.characterName.equals(readingFor, ignoreCase = true) }
            currLineNum.value = if (firstUserIndex != -1) firstUserIndex + 1 else 1
        }
        answerVisible.value = false
    }

    fun nextSceneLine() {
        val state = uiState.value
        val lines = state.lineList
        val readingFor = state.scene.readingFor
        if (lines.isEmpty()) return
        
        if (state.isAtEnd) {
            restart()
            return
        }

        val currentIndex = currLineNum.value - 1

        if (readingFor.isBlank()) {
            currLineNum.value += 1
            answerVisible.value = false
            return
        }
        
        var nextUserIndex = -1
        for (i in (currentIndex + 1) until lines.size) {
            if (lines[i].characterName.equals(readingFor, ignoreCase = true)) {
                nextUserIndex = i
                break
            }
        }

        if (nextUserIndex != -1) {
            currLineNum.value = nextUserIndex + 1
        } else {
            // No more user lines, go to the last line as a cue
            currLineNum.value = lines.size
        }
        answerVisible.value = false
    }

    private fun restart() {
        viewModelScope.launch {
            val scene = schmRepo.getScene(sceneId).filterNotNull().first()
            val lines = schmRepo.getSceneLines(sceneId).first()
            if (lines.isNotEmpty()) {
                val sortedLines = lines.sortedBy { it.order }
                val firstUserIndex = sortedLines.indexOfFirst {
                    it.characterName.equals(scene.readingFor, ignoreCase = true)
                }
                currLineNum.value = if (firstUserIndex != -1) firstUserIndex + 1 else 1
            } else {
                currLineNum.value = 1
            }
            answerVisible.value = false
        }
    }

    fun toggleAnswer() {
        answerVisible.value = !answerVisible.value
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
    val isUserLine: Boolean = false,
    val isAtEnd: Boolean = false
)
