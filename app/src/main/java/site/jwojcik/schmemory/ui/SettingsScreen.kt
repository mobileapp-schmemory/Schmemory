package site.jwojcik.schmemory.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import site.jwojcik.schmemory.data.PreferenceStorage
import site.jwojcik.schmemory.data.AppPreferences

//SETTINGS TO ADD
//Light mode dark mode
//Font size --> for all screens
//Font type --> Happy Monkey / Century Gothic (Default)
//Other font options??
//How your speeches/scripts are organized when added

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .fillMaxSize(),
    onUpClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val store = PreferenceStorage(LocalContext.current)
    //val appPrefs = store.appPreferencesFlow.collectAsStateWithLifecycle(AppPreferences())
    val coroutineScope = rememberCoroutineScope()

    val prefs by store.appPreferencesFlow
        .collectAsStateWithLifecycle(
            initialValue = AppPreferences(
                darkMode = false,
                fontScale = 1f,
                dyslexicFont = false
            )
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    titleContentColor = Black,
                    navigationIconContentColor = Black,
                    actionIconContentColor = Black
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Dark Mode")
                Switch(
                    checked = prefs.darkMode,
                    onCheckedChange = {
                        coroutineScope.launch {
                            store.saveDarkMode(it)
                        }
                    }
                )
            }

            Column{
                Text("Font Size")
                Slider(
                    value = prefs.fontScale,
                    onValueChange = {
                        coroutineScope.launch {
                            store.saveFontScale(it)
                        }
                    },
                    valueRange = 0.8f..1.5f
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Dyslexic safe font")
                Switch(
                    checked = prefs.dyslexicFont,
                    onCheckedChange = {
                        coroutineScope.launch {
                            store.saveDyslexicFont(it)
                        }
                    }
                )
            }
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
