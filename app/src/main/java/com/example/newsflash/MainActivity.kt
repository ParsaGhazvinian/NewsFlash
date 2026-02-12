package com.example.newsflash

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import com.example.newsflash.data.datastore.DataStoreManager
import com.example.newsflash.model.Article
import com.example.newsflash.network.InternetReceiver
import com.example.newsflash.network.fetchNews
import com.example.newsflash.screen.HomePageScreen
import com.example.newsflash.ui.theme.NewsFlashTheme
import kotlinx.coroutines.launch

private var articles by mutableStateOf<List<Article>>(emptyList())
private lateinit var connectivityManager: ConnectivityManager
private lateinit var networkCallback: ConnectivityManager.NetworkCallback
private var firstLaunch = true

private lateinit var dataStoreManager: DataStoreManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        connectivityManager =
            getSystemService(ConnectivityManager::class.java)

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                if (firstLaunch) {
                    firstLaunch = false
                    return
                }
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Back Online", Toast.LENGTH_SHORT).show()

                    lifecycleScope.launch {
                        articles = fetchNews()
                    }
                }
            }

            override fun onLost(network: android.net.Network) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "No internet connection, try again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        dataStoreManager = DataStoreManager(this)

        setContent {
            val darkMode by dataStoreManager.darkModeFlow.collectAsState(initial = false)
            NewsFlashTheme(darkTheme = darkMode) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding)
//                    ) {
                        Navigator(HomePageScreen)
//                    }
//                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}