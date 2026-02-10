package com.example.newsflash.network

private val api = RetrofitClient.retrofit.create(NewsApiService::class.java)

suspend fun fetchNews(){
    try {
        val response = api.getTopHeadlines(
            country = "us",
            category = "technology"
        )
//        println(response.)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}