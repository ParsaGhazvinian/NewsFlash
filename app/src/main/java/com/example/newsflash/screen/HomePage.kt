package com.example.newsflash.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.newsflash.model.Article
import com.example.newsflash.network.fetchNews
import com.example.newsflash.component.ArticleCard
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun HomePage() {
    val navigator = LocalNavigator.current
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

//    LaunchedEffect(Unit) {
//        scope.launch {
//            isLoading = true
//
//            val deferredArticles = async {
//                fetchNews()
//            }
//
//            articles = deferredArticles.await()
//            isLoading = false
//        }
//    }
    LaunchedEffect(Unit) {
        isLoading = true
        articles = fetchNews()
        isLoading = false
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        items = articles) { article ->
                        ArticleCard(
                            article = article,
                            onClick = {
                                navigator?.push(
                                    ArticleDetailsScreen(
                                        article.source,
                                        article.author,
                                        article.title,
                                        article.description,
                                        article.url,
                                        article.urlToImage,
                                        article.publishedAt,
                                        article.content
                                    )
                                )
                            })
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 0.0f,
                                endY = 100f
                            )
                        )
                        .padding(start = 8.dp, end = 8.dp)
                )
            }

        }
    }
}
