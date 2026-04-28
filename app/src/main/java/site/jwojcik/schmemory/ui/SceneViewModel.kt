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
import site.jwojcik.schmemory.data.SceneDataSource
import site.jwojcik.schmemory.data.SchmemoryRepository
import site.jwojcik.schmemory.data.SpeechDataSource
import kotlin.collections.get


class SceneViewModel(
    savedStateHandle: SavedStateHandle,
    private val schmRepo: SchmemoryRepository
) : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SchmemoryApplication)
                SceneScreen(this.createSavedStateHandle(), application.schmemoryRepository)
            }
        }
    }

    // Get from composable's route arguments
    private val sceneId: Long = savedStateHandle.toRoute<Routes.Scene>().sceneId

    private val currLineNum = MutableStateFlow(1)
    private val currLine = MutableStateFlow(Line(id = 0L))
    private val answerVisible = MutableStateFlow(true)

    val uiState: StateFlow<QuestionScreenUiState> =
        combine(
            studyRepo.getSubject(subjectId).filterNotNull(),
            studyRepo.getQuestions(subjectId),
            currQuestionNum,
            currQuestion,
            answerVisible
        ) { subject, questions, currNum, currQuest, ansVisible ->
            QuestionScreenUiState(
                subject = subject,
                questionList = questions,
                currQuestion =
                    if (currQuest.id == 0L && questions.isNotEmpty()) questions.first()
                    else if (questions.isEmpty()) currQuest
                    else questions[currNum - 1],
                currQuestionNum = currNum,
                totalQuestions = questions.size,
                answerVisible = ansVisible
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = QuestionScreenUiState(),
            )

    fun prevQuestion() {
        val index = (uiState.value.currQuestionNum - 2 + uiState.value.totalQuestions) %
                uiState.value.totalQuestions
        currQuestion.value = uiState.value.questionList[index]
        currQuestionNum.value = index + 1
    }

    fun nextQuestion() {
        val index = uiState.value.currQuestionNum % uiState.value.totalQuestions
        currQuestion.value = uiState.value.questionList[index]
        currQuestionNum.value = index + 1
    }

    fun deleteQuestion() {
        // TODO: Complete this function
    }

    fun toggleAnswer() {
        answerVisible.value = !answerVisible.value
    }
}

data class QuestionScreenUiState(
    val subject: Subject = Subject(),
    val currQuestion: Question = Question(),
    val questionList: List<Question> = emptyList(),
    val currQuestionNum: Int = 1,
    val totalQuestions: Int = 0,
    val answerVisible: Boolean = true
)
