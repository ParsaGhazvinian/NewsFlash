package com.example.newsflash.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.newsflash.component.ArticleCard
import com.example.newsflash.data.datastore.DataStoreManager
import com.example.newsflash.model.Article
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPage(
    articles: List<Article>
) {
    val context = LocalContext.current
    val navigator = LocalNavigator.current
    val dataStore = remember { DataStoreManager(context) }
    val historyList by dataStore.openedNewsFlow.collectAsState(initial = emptyList())
    val reversedList = historyList.reversed()
    var showConfirmDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History",style = MaterialTheme.typography.headlineMedium,) },
                navigationIcon = {
                    IconButton(onClick = { navigator?.pop() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showConfirmDialog = true }) {
                        Icon(Icons.Default.ClearAll, contentDescription = "Clear All",
                            modifier = Modifier.size(26.dp))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Text("You haven't opened any articles yet 📰")
                }

            } else {

                LazyColumn(
                    contentPadding = PaddingValues(
                        top = 8.dp, bottom = 80.dp
                    )
                ) {
                    items(reversedList) { article ->

                        ArticleCard(
                            article = article, onClick = {
                                navigator?.push(
                                    ArticleDetailsScreen(article)
                                )
                            })
                    }

                }
            }
            if (showConfirmDialog) {
                AlertDialog(
                    modifier = Modifier.height(180.dp),
                    onDismissRequest = { showConfirmDialog = false },
                    title = {
                        Text(
                            "Confirm Clearing History",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    text = {
                        Text(
                            "Do you want to clear all your history?",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.bodyLarge

                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    dataStore.clearHistory()
                                }
                                showConfirmDialog = false
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                "Clear",
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmDialog = false }) {
                            Text(
                                "Cancel",
                                fontSize = 16.sp,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    })

            }
        }
    }
}