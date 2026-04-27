package site.jwojcik.schmemory.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Green
import site.jwojcik.schmemory.ui.theme.Yellow
import site.jwojcik.schmemory.ui.theme.Purple
import site.jwojcik.schmemory.ui.theme.Gray
import site.jwojcik.schmemory.ui.theme.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

//SETTINGS TO ADD
//Light mode dark mode
//Font size --> for all screens
//Font type --> Happy Monkey / Century Gothic (Default)
//Other font options??
//How your speeches/scripts are organized when added

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier.background(Yellow).fillMaxSize(),
    onUpClick: () -> Unit = {}
) {
    val store = PreferenceStorage(LocalContext.current)
    //val appPrefs = store.appPreferencesFlow.collectAsStateWithLifecycle(AppPreferences())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = Black,
                ),
                navigationIcon = {
                    IconButton(onClick = onUpClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen(
        onUpClick = {
            true
        },
    )
}

class PreferenceStorage(private val context: Context) {

}