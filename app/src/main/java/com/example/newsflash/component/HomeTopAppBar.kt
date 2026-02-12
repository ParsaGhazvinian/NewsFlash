package com.example.newsflash.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.newsflash.data.datastore.DataStoreManager
import com.example.newsflash.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    isDarkMode: Boolean,
    article: List<Article>,
    onHistoryClick: () -> Unit
) {
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }
    var isToggled by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = "NewsFlash",
                style = MaterialTheme.typography.headlineMedium,
            )
        },
        actions = {
            IconButton(onClick = onHistoryClick) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    Modifier.size(26.dp)
                )
            }
            SettingDialog(isDarkMode)
        }
    )
}
