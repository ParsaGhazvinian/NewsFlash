package com.example.newsflash.network

import com.example.newsflash.model.Article

private val api = RetrofitClient.retrofit.create(NewsApiService::class.java)

suspend fun fetchNews(): List<Article> {
    return try {
        val response = api.getTopHeadlines(
            country = "us",
            category = "technology"
        )
        response.articles
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
