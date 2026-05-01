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
import kotlinx.coroutines.launch
import site.jwojcik.schmemory.Routes
import site.jwojcik.schmemory.SchmemoryApplication
import site.jwojcik.schmemory.data.SchmemoryRepository
import site.jwojcik.schmemory.data.Speech
import site.jwojcik.schmemory.data.SpeechLine

class SpeechViewModel(
    savedStateHandle: SavedStateHandle, schmRepo: SchmemoryRepository
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

    private var startTimeMillis: Long? = null
    private val _totalTimeMillis = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<SpeechLineScreenUiState> = combine(
        schmRepo.getSpeech(speechId).filterNotNull(),
        schmRepo.getSpeechLines(speechId),
        currLineNum,
        answerVisible,
        _totalTimeMillis
    ) { speech, lines, currNum, ansVisible, totalTime ->
        val sortedLines = lines.sortedBy { it.order }
        val isFinished = sortedLines.isNotEmpty() && currNum > sortedLines.size
        val currentLineIndex = currNum - 1
        val currentLine =
            if (isFinished) null else if (sortedLines.isNotEmpty() && currentLineIndex < sortedLines.size) sortedLines[currentLineIndex] else null

        // isAtEnd means we have revealed the last line or passed it
        val isAtEnd =
            sortedLines.isNotEmpty() && (isFinished || (currNum == sortedLines.size && ansVisible))

        SpeechLineScreenUiState(
            speech = speech,
            lineList = sortedLines,
            currSpeechLine = currentLine ?: SpeechLine(0, 0, 0, ""),
            previousLines = sortedLines.take(
                if (isFinished) sortedLines.size else currentLineIndex.coerceAtLeast(
                    0
                )
            ),
            currSpeechLineNum = if (isFinished) sortedLines.size else currNum,
            totalSpeechLines = sortedLines.size,
            answerVisible = ansVisible,
            isAtEnd = isAtEnd,
            isFinished = isFinished,
            totalTimeMillis = totalTime
        )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SpeechLineScreenUiState(
                speech = Speech(0, ""), currSpeechLine = SpeechLine(0, 0, 0, "")
            )
        )

    init {
        // Reactive timer stop logic
        viewModelScope.launch {
            uiState.collect { state ->
                if (state.isAtEnd && _totalTimeMillis.value == null) {
                    startTimeMillis?.let { start ->
                        _totalTimeMillis.value = System.currentTimeMillis() - start
                    }
                }
            }
        }
    }

    private fun startTimerIfNeeded() {
        if (startTimeMillis == null) {
            startTimeMillis = System.currentTimeMillis()
        }
    }

    fun prevSpeechLine() {
        startTimerIfNeeded()
        val state = uiState.value
        if (state.lineList.isEmpty()) return

        if (state.isFinished) {
            currLineNum.value = state.lineList.size
            answerVisible.value = true
            return
        }

        if (currLineNum.value > 1) {
            currLineNum.value -= 1
            answerVisible.value = false
        }
    }

    fun nextSpeechLine() {
        startTimerIfNeeded()
        val state = uiState.value
        if (state.lineList.isEmpty()) return

        if (state.isAtEnd) {
            restart()
            return
        }

        if (currLineNum.value <= state.lineList.size) {
            if (answerVisible.value) {
                currLineNum.value += 1
                answerVisible.value = false
            } else {
                answerVisible.value = true
            }
        }
    }

    private fun restart() {
        startTimeMillis = null
        _totalTimeMillis.value = null
        currLineNum.value = 1
        answerVisible.value = false
    }

    fun toggleAnswer() {
        startTimerIfNeeded()
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
    val answerVisible: Boolean = false,
    val isAtEnd: Boolean = false,
    val isFinished: Boolean = false,
    val totalTimeMillis: Long? = null
)
