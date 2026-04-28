package site.jwojcik.schmemory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import site.jwojcik.schmemory.data.AppPreferences
import site.jwojcik.schmemory.data.PreferenceStorage
import site.jwojcik.schmemory.ui.theme.SchmemoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val store = PreferenceStorage(this)
            val prefs by store.appPreferencesFlow.collectAsStateWithLifecycle(
                initialValue = AppPreferences(
                    darkMode = false,
                    fontScale = 1f,
                    dyslexicFont = false
                )
            )

            SchmemoryTheme (
                darkTheme = prefs.darkMode,
                fontScale = prefs.fontScale,
                dyslexicFont = prefs.dyslexicFont
            ) {
                SchmemoryApp()
            }
        }
    }
}