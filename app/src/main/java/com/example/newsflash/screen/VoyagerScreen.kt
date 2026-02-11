package com.example.newsflash.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.newsflash.model.Source

object HomePageScreen: Screen{
    @Composable
    override fun Content() {
        HomePage()
    }
}

data class ArticleDetailsScreen(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
) : Screen {
    @Composable
    override fun Content() {
        ArticleDetails(
            source,author,title,description,url,urlToImage,publishedAt,content
        )
    }
}
