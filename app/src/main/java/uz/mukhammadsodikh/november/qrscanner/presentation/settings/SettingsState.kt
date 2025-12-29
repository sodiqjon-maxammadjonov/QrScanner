package uz.mukhammadsodikh.november.qrscanner.presentation.settings

/**
 * Settings UI State
 */
data class SettingsUiState(
    val darkMode: Boolean = false,
    val vibration: Boolean = true,
    val sound: Boolean = true,
    val autoCopy: Boolean = false,
    val language: String = "en",
    val appVersion: String = "1.0.0"
)