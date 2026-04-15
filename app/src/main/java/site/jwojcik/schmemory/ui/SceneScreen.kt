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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.jwojcik.schmemory.data.Line
import site.jwojcik.schmemory.data.SceneDataSource
import site.jwojcik.schmemory.data.SceneLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SceneScreen(
    sceneId: Int,
    onUpClick: () -> Boolean,
    modifier: Modifier = Modifier,
    sceneDataSource: SceneDataSource = SceneDataSource()
) {
    val scene = sceneDataSource.getScene(sceneId)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = scene?.name ?: "Scene Name (err)"
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
                scene?.lines ?: listOf(
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
                        text = line.characterName.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = modifier.fillMaxWidth()
                    )
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
fun ScenePreview() {
    SceneScreen(
        sceneId = 0,
        onUpClick = {
            true
        },
    )
}