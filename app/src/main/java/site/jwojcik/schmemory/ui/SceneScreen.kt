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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import site.jwojcik.schmemory.data.SceneLine
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Yellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SceneScreen(
    viewModel: SceneViewModel = viewModel(
        factory = SceneViewModel.Factory
    ),
    onUpClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Yellow,
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = if (uiState.totalSceneLines == 0) uiState.scene.name 
                               else "${uiState.scene.name} (${uiState.currSceneLineNum}/${uiState.totalSceneLines})"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Blue)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Blue,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = viewModel::prevSceneLine) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                    }
                    
                    Button(
                        onClick = viewModel::toggleAnswer,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Blue)
                    ) {
                        Text(if (uiState.answerVisible) "Hide Line" else "Reveal Line")
                    }

                    IconButton(onClick = viewModel::nextSceneLine) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }
    ) { innerPadding ->
        if (uiState.totalSceneLines > 0) {
            RehearsalContent(
                previousLines = uiState.previousLines,
                currentLine = uiState.currSceneLine,
                readingFor = uiState.scene.readingFor,
                answerVisible = uiState.answerVisible,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No lines in this scene. Go to Edit to add some!")
            }
        }
    }
}

@Composable
fun RehearsalContent(
    previousLines: List<SceneLine>,
    currentLine: SceneLine,
    readingFor: String,
    answerVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(previousLines.size, answerVisible) {
        if (previousLines.isNotEmpty()) {
            listState.animateScrollToItem(previousLines.size)
        }
    }

    Column(modifier = modifier.padding(16.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(previousLines) { line ->
                LineItem(line = line, isUser = line.characterName.equals(readingFor, ignoreCase = true))
            }
            
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    Text(
                        text = currentLine.characterName.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    if (answerVisible) {
                        Text(
                            text = currentLine.text,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color.Black
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .fillMaxWidth()
                                .height(40.dp)
                                .background(Color.LightGray.copy(alpha = 0.3f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LineItem(line: SceneLine, isUser: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = line.characterName.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isUser) Blue else Color.Gray
        )
        Text(
            text = line.text,
            fontSize = 18.sp,
            color = if (isUser) Color.Black else Color.DarkGray,
            modifier = Modifier.padding(top = 2.dp)
        )
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp, color = Color.LightGray)
    }
}
