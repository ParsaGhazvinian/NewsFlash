package com.example.newsflash.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.newsflash.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManager(private val context: Context) {
    private val gson = Gson()

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

        val OPENED_NEWS_SET = stringSetPreferencesKey("opened_news_set")

        private val LIVE_UPDATES = booleanPreferencesKey("live_updates")

    }

    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { it ->
            it[DARK_MODE_KEY] = isDark
        }
    }

    val darkModeFlow: Flow<Boolean> =
        context.dataStore.data.map { it ->
            it[DARK_MODE_KEY] ?: false
        }

    suspend fun addOpenedNews(article: Article) {
        context.dataStore.edit { it ->
            val currentSet = it[OPENED_NEWS_SET] ?: emptySet()
            val articleJson = gson.toJson(article)
            it[OPENED_NEWS_SET] = currentSet + articleJson
        }
    }

    //    val openedNewsFlow: Flow<Set<String>> = context.dataStore.data
//        .map { preferences ->
//            preferences[OPENED_NEWS_SET] ?: emptySet()
//        }
//    val openedNewsFlow: Flow<List<Article>> =
//        context.dataStore.data.map { it ->
//
//            val jsonSet = it[OPENED_NEWS_SET] ?: emptySet()
//
//            jsonSet.map { json ->
//                gson.fromJson(json, Article::class.java)
//            }
//        }
    val openedNewsFlow: Flow<List<Article>> =
        context.dataStore.data.map { it ->

            val jsonSet = it[OPENED_NEWS_SET] ?: emptySet()

            jsonSet.mapNotNull { json ->
                try {
                    gson.fromJson(json, Article::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        }


    suspend fun clearHistory() {
        context.dataStore.edit { it ->
            it.remove(OPENED_NEWS_SET)
        }
    }

    suspend fun saveLiveUpdates(enabled: Boolean) {
        context.dataStore.edit { it ->
            it[LIVE_UPDATES] = enabled
        }
    }

    val liveUpdatesFlow: Flow<Boolean> = context.dataStore.data
        .map { it ->
            it[LIVE_UPDATES] ?: false
        }

}