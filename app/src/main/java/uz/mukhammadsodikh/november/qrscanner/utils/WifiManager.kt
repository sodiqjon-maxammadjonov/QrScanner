package uz.mukhammadsodikh.november.qrscanner.utils


import android.content.Context
import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

object WiFiConnectionManager {

    /**
     * Connect to WiFi (Android 10+)
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun connectToWiFi(
        context: Context,
        ssid: String,
        password: String,
        security: String,
        onResult: (Boolean, String) -> Unit
    ) {
        try {
            val specifier = WifiNetworkSpecifier.Builder()
                .setSsid(ssid)
                .apply {
                    when (security.uppercase()) {
                        "WPA", "WPA2", "WPA3" -> setWpa2Passphrase(password)
                        "WEP" -> setWpa2Passphrase(password) // WEP deprecated
                        "NOPASS", "" -> {} // Open network
                        else -> setWpa2Passphrase(password)
                    }
                }
                .build()

            val request = NetworkRequest.Builder()
                .addTransportType(android.net.NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(specifier)
                .build()

            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    super.onAvailable(network)
                    connectivityManager.bindProcessToNetwork(network)
                    onResult(true, "Connected to $ssid")
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    onResult(false, "Failed to connect to $ssid")
                }
            }

            connectivityManager.requestNetwork(request, networkCallback)

        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false, "Error: ${e.message}")
        }
    }

    /**
     * Connect to WiFi (Android 9 and below) - Legacy
     */
    @Suppress("DEPRECATION")
    fun connectToWiFiLegacy(
        context: Context,
        ssid: String,
        password: String,
        security: String,
        onResult: (Boolean, String) -> Unit
    ) {
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            if (!wifiManager.isWifiEnabled) {
                // WiFi o'chiq bo'lsa, settings'ga o'tkazish
                openWiFiSettings(context)
                onResult(false, "WiFi is disabled. Please enable it.")
                return
            }

            val wifiConfig = WifiConfiguration().apply {
                SSID = "\"$ssid\""

                when (security.uppercase()) {
                    "WPA", "WPA2" -> {
                        preSharedKey = "\"$password\""
                        allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                    }
                    "WEP" -> {
                        wepKeys[0] = "\"$password\""
                        wepTxKeyIndex = 0
                        allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                        allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
                    }
                    "NOPASS", "" -> {
                        allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                    }
                    else -> {
                        preSharedKey = "\"$password\""
                        allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                    }
                }
            }

            val networkId = wifiManager.addNetwork(wifiConfig)
            if (networkId != -1) {
                wifiManager.disconnect()
                wifiManager.enableNetwork(networkId, true)
                wifiManager.reconnect()
                onResult(true, "Connecting to $ssid...")
            } else {
                onResult(false, "Failed to add network")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false, "Error: ${e.message}")
        }
    }

    /**
     * Open WiFi settings
     */
    fun openWiFiSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * Smart connect - version check
     */
    fun smartConnect(
        context: Context,
        ssid: String,
        password: String,
        security: String,
        onResult: (Boolean, String) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectToWiFi(context, ssid, password, security, onResult)
        } else {
            connectToWiFiLegacy(context, ssid, password, security, onResult)
        }
    }
}