package site.jwojcik.schmemory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import site.jwojcik.schmemory.SchmemoryApplication
import site.jwojcik.schmemory.data.Line
import site.jwojcik.schmemory.data.Scene
import site.jwojcik.schmemory.data.SceneLine
import site.jwojcik.schmemory.data.SchmemoryRepository
import site.jwojcik.schmemory.data.Script
import site.jwojcik.schmemory.data.Speech
import site.jwojcik.schmemory.data.SpeechLine

data class EditUiState(
    val script: Script? = null,
    val lines: List<Line> = emptyList()
)

class EditViewModel(private val schmRepo: SchmemoryRepository) : ViewModel() {

    private val _scriptId = MutableStateFlow<Long?>(null)
    private val _listType = MutableStateFlow<SchmemoryListType?>(null)

    fun setScript(id: Long, type: SchmemoryListType) {
        _scriptId.value = id
        _listType.value = type
    }

    val uiState: StateFlow<EditUiState> = combine(_scriptId, _listType) { id, type ->
        id to type
    }.flatMapLatest { (id, type) ->
        if (id == null || type == null) {
            MutableStateFlow(EditUiState())
        } else {
            if (type == SchmemoryListType.SCENE) {
                combine(schmRepo.getScene(id), schmRepo.getSceneLines(id)) { scene, lines ->
                    EditUiState(scene, lines.sortedBy { it.order })
                }
            } else {
                combine(schmRepo.getSpeech(id), schmRepo.getSpeechLines(id)) { speech, lines ->
                    EditUiState(speech, lines.sortedBy { it.order })
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = EditUiState()
    )

    fun updateScriptName(newName: String) {
        val currentState = uiState.value
        val script = currentState.script ?: return
        viewModelScope.launch {
            if (script is Scene) {
                schmRepo.updateScene(script.copy(name = newName))
            } else if (script is Speech) {
                schmRepo.updateSpeech(script.copy(name = newName))
            }
        }
    }

    fun updateLineText(line: Line, newText: String) {
        viewModelScope.launch {
            if (line is SceneLine) {
                schmRepo.updateSceneLine(line.copy(text = newText))
            } else if (line is SpeechLine) {
                schmRepo.updateSpeechLine(line.copy(text = newText))
            }
        }
    }

    fun updateCharacterName(line: SceneLine, newName: String) {
        viewModelScope.launch {
            schmRepo.updateSceneLine(line.copy(characterName = newName))
        }
    }

    fun addLine() {
        val currentState = uiState.value
        val id = _scriptId.value ?: return
        val type = _listType.value ?: return
        val nextOrder = (currentState.lines.size).toLong()

        viewModelScope.launch {
            if (type == SchmemoryListType.SCENE) {
                schmRepo.addSceneLine(SceneLine(sceneId = id, order = nextOrder, characterName = "", text = ""))
            } else {
                schmRepo.addSpeechLine(SpeechLine(speechId = id, order = nextOrder, text = ""))
            }
        }
    }

    fun deleteLine(line: Line) {
        viewModelScope.launch {
            if (line is SceneLine) {
                schmRepo.deleteSceneLine(line)
            } else if (line is SpeechLine) {
                schmRepo.deleteSpeechLine(line)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SchmemoryApplication)
                EditViewModel(application.schmemoryRepository)
            }
        }
    }
}
