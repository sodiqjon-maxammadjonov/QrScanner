package uz.mukhammadsodikh.november.qrscanner.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import uz.mukhammadsodikh.november.qrscanner.utils.AdMobManager

@Composable
fun BannerAd(
    adMobManager: AdMobManager,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            adMobManager.createBannerAdView()
        },
        modifier = modifier.fillMaxWidth()
    )
}