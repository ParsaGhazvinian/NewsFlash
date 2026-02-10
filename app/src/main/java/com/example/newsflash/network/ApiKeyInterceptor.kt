package com.example.newsflash.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .addHeader("X-Api-Key", "74357e9921564786bbff07dfa782c94d") // <-- API KEY GOES HERE
            .build()

        return chain.proceed(newRequest)
    }
}
