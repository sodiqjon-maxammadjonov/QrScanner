package uz.mukhammadsodikh.november.qrscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import uz.mukhammadsodikh.november.qrscanner.core.design.theme.QRScannerTheme
import uz.mukhammadsodikh.november.qrscanner.data.preferences.PreferencesManager
import uz.mukhammadsodikh.november.qrscanner.presentation.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val preferencesManager = PreferencesManager(this)
        setContent {
            val darkMode by preferencesManager.darkMode.collectAsState(initial = false)
            QRScannerTheme(
                darkTheme = darkMode
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)

                ) {
                    MainScreen()
                }
            }
        }
    }
}