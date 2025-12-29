package uz.mukhammadsodikh.november.qrscanner.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore instance
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * Preferences Manager - Settings saqlash
 */
class PreferencesManager(private val context: Context) {

    companion object {
        // Keys
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
        private val VIBRATION = booleanPreferencesKey("vibration")
        private val SOUND = booleanPreferencesKey("sound")
        private val AUTO_COPY = booleanPreferencesKey("auto_copy")
        private val LANGUAGE = stringPreferencesKey("language")
    }

    /**
     * Dark Mode
     */
    val darkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: false // Default: Light mode
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }

    /**
     * Vibration
     */
    val vibration: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[VIBRATION] ?: true // Default: On
    }

    suspend fun setVibration(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[VIBRATION] = enabled
        }
    }

    /**
     * Sound
     */
    val sound: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SOUND] ?: true // Default: On
    }

    suspend fun setSound(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SOUND] = enabled
        }
    }

    /**
     * Auto Copy
     */
    val autoCopy: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_COPY] ?: false // Default: Off
    }

    suspend fun setAutoCopy(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_COPY] = enabled
        }
    }

    /**
     * Language
     */
    val language: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: "en" // Default: English
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = lang
        }
    }
}