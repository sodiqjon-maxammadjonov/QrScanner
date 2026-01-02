package uz.mukhammadsodikh.november.qrscanner.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.mukhammadsodikh.november.qrscanner.data.preferences.PreferencesManager

class SettingsViewModel(context: Context) : ViewModel() {

    private val preferencesManager = PreferencesManager(context)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            combine(
                preferencesManager.darkMode,
                preferencesManager.vibration,
                preferencesManager.sound,
                preferencesManager.autoCopy,
                preferencesManager.language
            ) { darkMode, vibration, sound, autoCopy, language ->
                SettingsUiState(
                    darkMode = darkMode,
                    vibration = vibration,
                    sound = sound,
                    autoCopy = autoCopy,
                    language = language
                )
            }.collectLatest { newState ->
                _uiState.value = newState
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            preferencesManager.setDarkMode(!_uiState.value.darkMode)
        }
    }

    fun toggleVibration() {
        viewModelScope.launch {
            preferencesManager.setVibration(!_uiState.value.vibration)
        }
    }

    fun toggleSound() {
        viewModelScope.launch {
            preferencesManager.setSound(!_uiState.value.sound)
        }
    }

    fun toggleAutoCopy() {
        viewModelScope.launch {
            preferencesManager.setAutoCopy(!_uiState.value.autoCopy)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferencesManager.setLanguage(language)
        }
    }
    fun getShareText(packageName: String): String {
        val appLink = "https://play.google.com/store/apps/details?id=$packageName"
        return "Check out this amazing QR Scanner app! Download it here: $appLink"
    }
}