package site.jwojcik.schmemory.ui

import androidx.compose.remote.creation.first
import androidx.compose.ui.geometry.isEmpty
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import androidx.preference.isNotEmpty
import androidx.preference.size
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
                // Corrected: Call SceneViewModel constructor, not SceneScreen
                SceneViewModel(this.createSavedStateHandle(), application.schmemoryRepository)
            }
        }
    }

    private val sceneId: Long = savedStateHandle.toRoute<Routes.Scene>().sceneId

    private val currLineNum = MutableStateFlow(1)
    private val currLine = MutableStateFlow(
        SceneLine(id = 0L, sceneId = 0, order = 0, characterName = "", text = "")
    )
    private val answerVisible = MutableStateFlow(true)

    // Corrected: Consistent naming with SceneLineScreenUiState
    val uiState: StateFlow<SceneLineScreenUiState> =
        combine(
            schmRepo.getScene(sceneId).filterNotNull(),
            schmRepo.getSceneLines(sceneId),
            currLineNum,
            currLine,
            answerVisible
        ) { scene, lines, currNum, line, ansVisible ->
            SceneLineScreenUiState(
                scene = scene,
                lineList = lines,
                currSceneLine = if (line.id == 0L && lines.isNotEmpty()) lines.first()
                else if (lines.isEmpty()) line
                else lines.getOrElse(currNum - 1) { lines.first() },
                currSceneLineNum = currNum,
                totalSceneLines = lines.size,
                answerVisible = ansVisible
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                // Corrected: Added required initial values
                initialValue = SceneLineScreenUiState(
                    scene = Scene(0, ""),
                    currSceneLine = SceneLine(0, 0, 0, "", "")
                )
            )

    fun prevSceneLine() {
        if (uiState.value.totalSceneLines == 0) return
        val index = (uiState.value.currSceneLineNum - 2 + uiState.value.totalSceneLines) %
                uiState.value.totalSceneLines
        currLine.value = uiState.value.lineList[index]
        currLineNum.value = index + 1
    }

    fun nextSceneLine() {
        if (uiState.value.totalSceneLines == 0) return
        val index = uiState.value.currSceneLineNum % uiState.value.totalSceneLines
        currLine.value = uiState.value.lineList[index]
        currLineNum.value = index + 1
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
    val currSceneLineNum: Int = 1,
    val totalSceneLines: Int = 0,
    val answerVisible: Boolean = true
)