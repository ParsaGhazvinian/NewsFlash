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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.newsflash.model.Article
import com.example.newsflash.network.fetchNews
import com.example.newsflash.component.ArticleCard
import com.example.newsflash.component.HomeTopBar
import com.example.newsflash.data.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun HomePage() {
    val navigator = LocalNavigator.current
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    val isDark by dataStore.darkModeFlow.collectAsState(initial = false)
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

    Scaffold(
        topBar = {
            HomeTopBar(
                isDarkMode = isDark,
                articles,
                onHistoryClick = {
                    navigator?.push(HistoryPageScreen(articles))
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = 80.dp
                        )
                    ) {
                        items(
                            items = articles
                        ) { article ->
                            ArticleCard(
                                article = article,
                                onClick = {
                                    navigator?.push(
                                        ArticleDetailsScreen(article)
                                    )
                                })
                        }
                    }
                }

            }
        }
    }
}

