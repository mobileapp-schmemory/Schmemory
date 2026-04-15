package site.jwojcik.schmemory.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import site.jwojcik.schmemory.data.SceneDataSource
import site.jwojcik.schmemory.data.Script
import site.jwojcik.schmemory.data.SpeechDataSource

enum class SchmemoryListType { SCENE, SPEECH }

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


    Scaffold { innerPadding ->
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                onItemClick(script.id)
            }) {
                Text(
                    text = script.name,
                    modifier = modifier.padding(start = 12.dp),
                    color = Color.Black
                )
            }
        }
    }
}