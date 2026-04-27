package site.jwojcik.schmemory.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PreferenceStorage(private val context: Context)
{
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "app_prefs")

        private object PreferenceKeys {
            val DARK_MODE = booleanPreferencesKey("dark_mode")
            val FONT_SCALE = floatPreferencesKey("font_scale")
            val DYSLEXIC_SAFE_FONT = booleanPreferencesKey("dyslexic_safe_font")
        }
    }

    val appPreferencesFlow = context.dataStore.data.map { preferences ->

        val darkMode = preferences[PreferenceKeys.DARK_MODE] ?: false
        val fontScale = preferences[PreferenceKeys.FONT_SCALE] ?: 1f
        val dyslexicFont = preferences[PreferenceKeys.DYSLEXIC_SAFE_FONT] ?: false

        AppPreferences(
            darkMode = darkMode,
            fontScale = fontScale,
            dyslexicFont = dyslexicFont
        )
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DARK_MODE] = enabled
        }
    }

    suspend fun saveFontScale(scale: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.FONT_SCALE] = scale
        }
    }

    suspend fun saveDyslexicFont(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.DYSLEXIC_SAFE_FONT] = enabled
        }
    }
}