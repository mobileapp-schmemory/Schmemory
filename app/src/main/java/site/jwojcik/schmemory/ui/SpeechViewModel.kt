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
import site.jwojcik.schmemory.data.SchmemoryRepository
import site.jwojcik.schmemory.data.Speech
import site.jwojcik.schmemory.data.SpeechLine

class SpeechViewModel(
    savedStateHandle: SavedStateHandle,
    private val schmRepo: SchmemoryRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SchmemoryApplication)
                SpeechViewModel(this.createSavedStateHandle(), application.schmemoryRepository)
            }
        }
    }

    private val speechId: Long = savedStateHandle.toRoute<Routes.Speech>().speechId
    private val currLineNum = MutableStateFlow(1)
    private val answerVisible = MutableStateFlow(false)

    val uiState: StateFlow<SpeechLineScreenUiState> =
        combine(
            schmRepo.getSpeech(speechId).filterNotNull(),
            schmRepo.getSpeechLines(speechId),
            currLineNum,
            answerVisible
        ) { speech, lines, currNum, ansVisible ->
            val sortedLines = lines.sortedBy { it.order }
            val currentLineIndex = (currNum - 1).coerceIn(0, (sortedLines.size - 1).coerceAtLeast(0))
            val currentLine = if (sortedLines.isNotEmpty()) sortedLines[currentLineIndex] else null

            SpeechLineScreenUiState(
                speech = speech,
                lineList = sortedLines,
                currSpeechLine = currentLine ?: SpeechLine(0, 0, 0, ""),
                previousLines = if (currentLine != null) sortedLines.take(currentLineIndex) else emptyList(),
                currSpeechLineNum = currNum,
                totalSpeechLines = sortedLines.size,
                answerVisible = ansVisible
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = SpeechLineScreenUiState(
                    speech = Speech(0, ""),
                    currSpeechLine = SpeechLine(0, 0, 0, "")
                )
            )

    fun prevSpeechLine() {
        val state = uiState.value
        if (state.lineList.isEmpty()) return
        
        currLineNum.value = if (currLineNum.value > 1) currLineNum.value - 1 else state.lineList.size
        answerVisible.value = false
    }

    fun nextSpeechLine() {
        val state = uiState.value
        if (state.lineList.isEmpty()) return
        
        currLineNum.value = if (currLineNum.value < state.lineList.size) currLineNum.value + 1 else 1
        answerVisible.value = false
    }

    fun toggleAnswer() {
        answerVisible.value = !answerVisible.value
    }
}

data class SpeechLineScreenUiState(
    val speech: Speech,
    val currSpeechLine: SpeechLine,
    val lineList: List<SpeechLine> = emptyList(),
    val previousLines: List<SpeechLine> = emptyList(),
    val currSpeechLineNum: Int = 1,
    val totalSpeechLines: Int = 0,
    val answerVisible: Boolean = false
)
