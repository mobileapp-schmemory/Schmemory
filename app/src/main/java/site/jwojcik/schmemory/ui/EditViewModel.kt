package site.jwojcik.schmemory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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
    val lines: List<Line> = emptyList(),
    val hasUnsavedChanges: Boolean = false
)

class EditViewModel(private val schmRepo: SchmemoryRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(EditUiState())
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    private val deletedLines = mutableListOf<Line>()
    private var scriptId: Long? = null
    private var listType: SchmemoryListType? = null

    fun setScript(id: Long, type: SchmemoryListType) {
        if (scriptId == id && listType == type) return
        scriptId = id
        listType = type

        viewModelScope.launch {
            if (type == SchmemoryListType.SCENE) {
                val scene = schmRepo.getScene(id).first()
                val lines = schmRepo.getSceneLines(id).first()
                _uiState.value = EditUiState(scene, lines.sortedBy { it.order }, hasUnsavedChanges = false)
            } else {
                val speech = schmRepo.getSpeech(id).first()
                val lines = schmRepo.getSpeechLines(id).first()
                _uiState.value = EditUiState(speech, lines.sortedBy { it.order }, hasUnsavedChanges = false)
            }
        }
    }

    fun updateScriptName(newName: String) {
        _uiState.update { currentState ->
            val updatedScript = when (val script = currentState.script) {
                is Scene -> script.copy(name = newName)
                is Speech -> script.copy(name = newName)
                else -> script
            }
            currentState.copy(script = updatedScript, hasUnsavedChanges = true)
        }
    }

    fun updateLineText(line: Line, newText: String) {
        _uiState.update { currentState ->
            val updatedLines = currentState.lines.map {
                if (it === line) {
                    when (it) {
                        is SceneLine -> it.copy(text = newText)
                        is SpeechLine -> it.copy(text = newText)
                        else -> it
                    }
                } else it
            }
            currentState.copy(lines = updatedLines, hasUnsavedChanges = true)
        }
    }

    fun updateCharacterName(line: SceneLine, newName: String) {
        _uiState.update { currentState ->
            val updatedLines = currentState.lines.map {
                if (it === line) {
                    (it as SceneLine).copy(characterName = newName)
                } else it
            }
            currentState.copy(lines = updatedLines, hasUnsavedChanges = true)
        }
    }

    fun addLine() {
        val id = scriptId ?: return
        val type = listType ?: return

        _uiState.update { currentState ->
            val nextOrder = currentState.lines.size.toLong()
            val newLine = if (type == SchmemoryListType.SCENE) {
                SceneLine(sceneId = id, order = nextOrder, characterName = "", text = "")
            } else {
                SpeechLine(speechId = id, order = nextOrder, text = "")
            }
            currentState.copy(lines = currentState.lines + newLine, hasUnsavedChanges = true)
        }
    }

    fun deleteLine(line: Line) {
        if (line.id != 0L) {
            deletedLines.add(line)
        }
        _uiState.update { currentState ->
            currentState.copy(lines = currentState.lines - line, hasUnsavedChanges = true)
        }
    }

    fun save(onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        val script = state.script ?: return

        viewModelScope.launch {
            if (script is Scene) {
                schmRepo.updateScene(script)
            } else if (script is Speech) {
                schmRepo.updateSpeech(script)
            }

            deletedLines.forEach { line ->
                if (line is SceneLine) {
                    schmRepo.deleteSceneLine(line)
                } else if (line is SpeechLine) {
                    schmRepo.deleteSpeechLine(line)
                }
            }
            deletedLines.clear()

            state.lines.forEachIndexed { index, line ->
                // Ensure order is updated
                val updatedLine = when (line) {
                    is SceneLine -> line.copy(order = index.toLong())
                    is SpeechLine -> line.copy(order = index.toLong())
                    else -> line
                }

                if (updatedLine.id == 0L) {
                    if (updatedLine is SceneLine) {
                        schmRepo.addSceneLine(updatedLine)
                    } else if (updatedLine is SpeechLine) {
                        schmRepo.addSpeechLine(updatedLine)
                    }
                } else {
                    if (updatedLine is SceneLine) {
                        schmRepo.updateSceneLine(updatedLine)
                    } else if (updatedLine is SpeechLine) {
                        schmRepo.updateSpeechLine(updatedLine)
                    }
                }
            }
            _uiState.update { it.copy(hasUnsavedChanges = false) }
            onSuccess()
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
