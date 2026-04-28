package site.jwojcik.schmemory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.jwojcik.schmemory.data.SceneLine

/*
@Composable
fun SpeechScreen(
    speechId: Int,
    modifier: Modifier = Modifier,
    speechDataSource: SpeechDataSource = SpeechDataSource(),
    onUpClick: () -> Boolean
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            QuestionTopBar(
                subjectTitle = uiState.value.subject.title,
                questionNum = uiState.value.currQuestionNum,
                totalQuestions = uiState.value.totalQuestions,
                onUpClick = onUpClick
            )
        },
        bottomBar = {
            QuestionBottomBar(
                showAddOnly = uiState.value.totalQuestions == 0,
                onAddClick = onAddClick,
                onEditClick = { onEditClick(uiState.value.currQuestion.id) },
                onDeleteClick = viewModel::deleteQuestion
            )
        }
    ) { innerPadding ->
        if (uiState.value.totalQuestions > 0) {
            QuestionAndAnswer(
                question = uiState.value.currQuestion,
                totalQuestions = uiState.value.totalQuestions,
                onPrevClick = viewModel::prevQuestion,
                onNextClick = viewModel::nextQuestion,
                onToggleAnswerClick = viewModel::toggleAnswer,
                answerVisible = uiState.value.answerVisible,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechScreen(//FullSpeechScreen(
    speechId: Long,
    modifier: Modifier = Modifier,
    speechDataSource: SpeechDataSource = SpeechDataSource(),
    onUpClick: () -> Boolean
) {
    val speech = speechDataSource.getSpeech(speechId)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = speech?.name ?: "Scene Name (err)"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onUpClick()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = modifier
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = modifier
                .padding(innerPadding)
                .padding(32.dp)
                .fillMaxWidth()
        ) {
            items(
                speech?.lines ?: listOf(
                    SceneLine(
                        text = "Line 1 (err)",
                        characterName = "Character Name (err)"
                    ), SceneLine(text = "Line 2 (err)", characterName = "Character Name (err)")
                )
            ) { line ->
                Column(
                    modifier = modifier.padding(bottom = 24.dp)
                ) {
                    Text(
                        text = line.text,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Left,
                        modifier = modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SpeechPreview() {
    SpeechScreen(
        speechId = 0,
        onUpClick = {
            true
        },
    )
}