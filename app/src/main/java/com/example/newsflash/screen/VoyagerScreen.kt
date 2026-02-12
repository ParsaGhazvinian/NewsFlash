package com.example.newsflash.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.newsflash.model.Article
import com.example.newsflash.model.Source

object HomePageScreen: Screen{
    @Composable
    override fun Content() {
        HomePage()
    }
}
data class HistoryPageScreen(
    val article: List<Article>
): Screen{
    @Composable
    override fun Content() {
        HistoryPage(article)
    }
}
data class ArticleDetailsScreen(
    val article: Article
) : Screen {
    @Composable
    override fun Content() {
        ArticleDetails(
            article
        )
    }
}
