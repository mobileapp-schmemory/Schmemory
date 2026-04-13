package site.jwojcik.schmemory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import site.jwojcik.schmemory.R
import site.jwojcik.schmemory.data.SceneDataSource

enum class SchmemoryListType { SCENE, SPEECH }

@Composable
fun ListScreen(
    modifier: Modifier,
    listType: SchmemoryListType,
    onUpClick: () -> Boolean,
    onItemClick: (Int) -> Unit
) {

    var scriptList: List<Any>

    if (listType == SchmemoryListType.SCENE) {
        scriptList = SceneDataSource().loadScenes()
    } else {
        //scriptList = SpeechDataSource().loadScenes()
    }


    Scaffold() { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ItemList(
                itemList = scriptList
            )
        }
    }
}

@Composable
fun ItemList(
    itemList: List<Any>
) {
    LazyColumn {
        items(
            items = itemList,
            key = { script -> script.id }
        ) { script ->
            val currentTask by rememberUpdatedState(script)

            ScriptCard(
                script = script
            )
        }
    }
}

@Composable
fun ScriptCard(
    script: Any,
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
            Text(
                text = script.title,
                modifier = modifier.padding(start = 12.dp),
                color = Color.Black
            )
        }
    }
}