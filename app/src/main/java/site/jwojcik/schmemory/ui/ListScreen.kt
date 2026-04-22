package site.jwojcik.schmemory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import site.jwojcik.schmemory.data.SceneDataSource
import site.jwojcik.schmemory.data.Script
import site.jwojcik.schmemory.data.SpeechDataSource
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Yellow
import site.jwojcik.schmemory.R

enum class SchmemoryListType { SCENE, SPEECH }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    listType: SchmemoryListType,
    onUpClick: () -> Boolean,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scriptList = when (listType) {
        SchmemoryListType.SCENE -> SceneDataSource().loadScenes()
        else -> SpeechDataSource().loadSpeeches()
    }

    Scaffold(containerColor = Yellow,
        modifier = modifier,
        topBar = { TopAppBar(
            title = { if("${listType.name}".equals("SPEECH")) {
                Text(text = "Speeches")
            } else {
                Text(text = "Scenes")
            }
                    },
            navigationIcon = {
                IconButton(onClick = { onUpClick() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Unspecified)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Blue
            ),
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Add")
                }
            },
            modifier = modifier
        )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ItemList(itemList = scriptList, onItemClick)
        }
    }
}

@Composable
fun ItemList(
    itemList: List<Script>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn {
        items(
            items = itemList,
            key = { script -> script.id }
        ) { script ->

            ScriptCard(
                script = script,
                onItemClick
            )
        }
    }
}

@Composable
fun ScriptCard(
    script: Script,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Yellow
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(Blue),
                onClick = {
                onItemClick(script.id)
            }) {
                Text(
                    text = script.name,
                    modifier = modifier.padding(start = 12.dp, end = 12.dp),
                    color = Color.Black
                )
            }
        }
    }
}