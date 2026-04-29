package site.jwojcik.schmemory.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import site.jwojcik.schmemory.data.SpeechLine

@Composable
fun SpeechScreen(
    viewModel: SpeechViewModel = viewModel(
        factory = SpeechViewModel.Factory
    ),
    onUpClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: (Long) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SpeechTopBar(
                subjectTitle = uiState.value.speech.name,
                speechLineNum = uiState.value.currSpeechLineNum,
                totalSpeechLines = uiState.value.totalSpeechLines,
                onUpClick = onUpClick
            )
        },
        bottomBar = {
            SpeechBottomBar(
                showAddOnly = uiState.value.totalSpeechLines == 0,
                onAddClick = onAddClick,
                onEditClick = { onEditClick(uiState.value.currSpeechLine.id) },
                onDeleteClick = viewModel::deleteSpeechLine
            )
        }
    ) { innerPadding ->
        if (uiState.value.totalSpeechLines > 0) {
            SpeechLineAndAnswer(
                speechLine = uiState.value.currSpeechLine,
                totalSpeechLines = uiState.value.totalSpeechLines,
                onPrevClick = viewModel::prevSpeechLine,
                onNextClick = viewModel::nextSpeechLine,
                onToggleAnswerClick = viewModel::toggleAnswer,
                answerVisible = uiState.value.answerVisible,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            )
        }
    }
}

@Composable
fun SpeechLineAndAnswer(
    speechLine: SpeechLine,
    totalSpeechLines: Int,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onToggleAnswerClick: () -> Unit,
    answerVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    val showAnswerBtnLabel = if (answerVisible) "Hide Answer" else "Show Answer"

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = "Q",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 80.sp,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = speechLine.text,
                fontSize = 30.sp,
                lineHeight = 34.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (totalSpeechLines > 1) {
                OutlinedIconButton(onClick = onPrevClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Previous")
                }
            } else {
                Spacer(Modifier)
            }
            Button(
                onClick = onToggleAnswerClick
            ) {
                Text(showAnswerBtnLabel)
            }
            if (totalSpeechLines > 1) {
                OutlinedIconButton(onClick = onNextClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Next")
                }
            } else {
                Spacer(Modifier)
            }
        }
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            if (answerVisible) {
                Text(
                    text = "A",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 80.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = speechLine.text,
                    fontSize = 30.sp,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSpeechLineAndAnswer() {
    SpeechLineAndAnswer(
        speechLine = SpeechLine(
            id=0,
            speechId=0,
            order=0,
            text = "Text"
        ),
        totalSpeechLines = 2,
        onPrevClick = {},
        onNextClick = {},
        onToggleAnswerClick = {},
        answerVisible = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechTopBar(
    subjectTitle: String,
    speechLineNum: Int,
    totalSpeechLines: Int,
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = if (totalSpeechLines == 0) "$subjectTitle (Empty)" else
        "$subjectTitle ($speechLineNum of $totalSpeechLines)"

    TopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onUpClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack,"Back")
            }
        }
    )
}

@Composable
fun SpeechBottomBar(
    showAddOnly: Boolean,
    onAddClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    BottomAppBar(
        actions = {
            if (!showAddOnly) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    )
}