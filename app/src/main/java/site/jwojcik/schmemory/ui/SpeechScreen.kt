package site.jwojcik.schmemory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import site.jwojcik.schmemory.data.SpeechLine
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Black
import site.jwojcik.schmemory.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeechScreen(
    viewModel: SpeechViewModel = viewModel(
        factory = SpeechViewModel.Factory
    ),
    onUpClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (uiState.totalSpeechLines == 0) uiState.speech.name 
                               else "${uiState.speech.name} (${uiState.currSpeechLineNum}/${uiState.totalSpeechLines})"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = Black,
                    navigationIconContentColor = Black,
                    actionIconContentColor = Black
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Blue,
                contentColor = Black
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = viewModel::prevSpeechLine) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                    }
                    
                    Button(
                        onClick = viewModel::toggleAnswer,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = Blue
                        )
                    ) {
                        Text(if (uiState.answerVisible) "Hide Line" else "Reveal Line")
                    }

                    IconButton(onClick = viewModel::nextSpeechLine) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (uiState.totalSpeechLines > 0) {
            SpeechRehearsalContent(
                previousLines = uiState.previousLines,
                currentLine = uiState.currSpeechLine,
                answerVisible = uiState.answerVisible,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(
                    "No lines in this speech. Go to Edit to add some!",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun SpeechRehearsalContent(
    previousLines: List<SpeechLine>,
    currentLine: SpeechLine,
    answerVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Automatically scroll to the bottom when new lines are added or your line is revealed
    LaunchedEffect(previousLines.size, answerVisible) {
        if (previousLines.isNotEmpty() || answerVisible) {
            val lastIndex = if (answerVisible) previousLines.size else (previousLines.size - 1).coerceAtLeast(0)
            listState.animateScrollToItem(lastIndex)
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(previousLines) { line ->
                SpeechLineItem(line = line)
            }
            
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Text(
                        text = "CURRENT LINE",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Blue
                    )
                    if (answerVisible) {
                        Text(
                            text = currentLine.text,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpeechLineItem(line: SpeechLine) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = line.text,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 2.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp), 
            thickness = 0.5.dp, 
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
        )
    }
}
