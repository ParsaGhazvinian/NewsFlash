package com.example.newsflash.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetReceiver(
    private val onInternetChange: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network =
            connectivityManager.activeNetwork

        val capabilities = connectivityManager.getNetworkCapabilities(network)

        val isConnected =
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

//            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        onInternetChange(isConnected)


    }
}