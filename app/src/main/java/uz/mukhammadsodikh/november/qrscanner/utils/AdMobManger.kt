package uz.mukhammadsodikh.november.qrscanner.utils

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * AdMob Manager - Smart ad qontroli
 */
class AdMobManager(private val context: Context) {

    private var interstitialAd: InterstitialAd? = null
    private var lastAdShowTime = 0L
    private var appOpenTime = System.currentTimeMillis()
    private var isScanning = false

    companion object {
        const val BANNER_AD_UNIT_ID = "ca-app-pub-2110406871216164/1642289031"
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-2110406871216164/7181733782"

        // Ad settings
        private const val MIN_INTERVAL_BETWEEN_ADS = 120000L // 2 daqiqa
        private const val INITIAL_DELAY = 120000L // Ilova ochilgandan 2 daqiqa
    }

    /**
     * Initialize AdMob
     */
    fun initialize() {
        MobileAds.initialize(context) {}
        loadInterstitialAd()
    }

    /**
     * Load Interstitial Ad
     */
    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            }
        )
    }

    /**
     * Set scanning state
     */
    fun setScanning(scanning: Boolean) {
        isScanning = scanning
    }

    /**
     * Show Interstitial Ad (Smart logic)
     */
    fun showInterstitialAd(activity: Activity, onAdClosed: (() -> Unit)? = null) {
        val currentTime = System.currentTimeMillis()

        if (isScanning) {
            onAdClosed?.invoke()
            return
        }

        if (currentTime - appOpenTime < INITIAL_DELAY) {
            onAdClosed?.invoke()
            return
        }

        if (currentTime - lastAdShowTime < MIN_INTERVAL_BETWEEN_ADS) {
            onAdClosed?.invoke()
            return
        }

        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    lastAdShowTime = System.currentTimeMillis()
                    interstitialAd = null
                    loadInterstitialAd() // Load next ad
                    onAdClosed?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    loadInterstitialAd()
                    onAdClosed?.invoke()
                }
            }

            ad.show(activity)
        } ?: run {
            onAdClosed?.invoke()
            loadInterstitialAd() // Try to load if not loaded
        }
    }

    /**
     * Create Banner Ad View
     */
    fun createBannerAdView(): AdView {
        return AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BANNER_AD_UNIT_ID
            loadAd(AdRequest.Builder().build())
        }
    }
}