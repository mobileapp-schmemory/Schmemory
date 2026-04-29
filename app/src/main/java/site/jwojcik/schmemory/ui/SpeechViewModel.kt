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
import site.jwojcik.schmemory.data.Speech
import site.jwojcik.schmemory.data.SpeechLine
import site.jwojcik.schmemory.data.SchmemoryRepository

class SpeechViewModel(
    savedStateHandle: SavedStateHandle,
    private val schmRepo: SchmemoryRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SchmemoryApplication)
                // Corrected: Call SpeechViewModel constructor, not SpeechScreen
                SpeechViewModel(this.createSavedStateHandle(), application.schmemoryRepository)
            }
        }
    }

    private val speechId: Long = savedStateHandle.toRoute<Routes.Speech>().speechId

    private val currLineNum = MutableStateFlow(1)
    private val currLine = MutableStateFlow(
        SpeechLine(id = 0L, speechId = 0, order = 0, text = "")
    )
    private val answerVisible = MutableStateFlow(true)

    // Corrected: Consistent naming with SpeechLineScreenUiState
    val uiState: StateFlow<SpeechLineScreenUiState> =
        combine(
            schmRepo.getSpeech(speechId).filterNotNull(),
            schmRepo.getSpeechLines(speechId),
            currLineNum,
            currLine,
            answerVisible
        ) { speech, lines, currNum, line, ansVisible ->
            SpeechLineScreenUiState(
                speech = speech,
                lineList = lines,
                currSpeechLine = if (line.id == 0L && lines.isNotEmpty()) lines.first()
                else if (lines.isEmpty()) line
                else lines.getOrElse(currNum - 1) { lines.first() },
                currSpeechLineNum = currNum,
                totalSpeechLines = lines.size,
                answerVisible = ansVisible
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                // Corrected: Added required initial values
                initialValue = SpeechLineScreenUiState(
                    speech = Speech(0, ""),
                    currSpeechLine = SpeechLine(0, 0, 0, "",)
                )
            )

    fun prevSpeechLine() {
        if (uiState.value.totalSpeechLines == 0) return
        val index = (uiState.value.currSpeechLineNum - 2 + uiState.value.totalSpeechLines) %
                uiState.value.totalSpeechLines
        currLine.value = uiState.value.lineList[index]
        currLineNum.value = index + 1
    }

    fun nextSpeechLine() {
        if (uiState.value.totalSpeechLines == 0) return
        val index = uiState.value.currSpeechLineNum % uiState.value.totalSpeechLines
        currLine.value = uiState.value.lineList[index]
        currLineNum.value = index + 1
    }

    fun toggleAnswer() {
        answerVisible.value = !answerVisible.value
    }

    fun deleteSpeechLine() {
        // TODO: Complete this function
    }
}

data class SpeechLineScreenUiState(
    val speech: Speech,
    val currSpeechLine: SpeechLine,
    val lineList: List<SpeechLine> = emptyList(),
    val currSpeechLineNum: Int = 1,
    val totalSpeechLines: Int = 0,
    val answerVisible: Boolean = true
)