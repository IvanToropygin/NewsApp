package com.example.domain.repository

import com.example.domain.entity.Article
import com.example.domain.entity.Language
import com.example.domain.entity.RefreshConfig
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun startBackgroundRefresh(refreshConfig: RefreshConfig)

    fun getAllSubscriptions(): Flow<List<String>>

    suspend fun addSubscription(topic: String)

    suspend fun updateArticlesForTopic(topic: String, language: Language): Boolean

    suspend fun removeSubscriptions(topic: String)

    suspend fun updateArticlesForAllSubscriptions(language: Language): List<String>

    fun getArticlesByTopics(topics: List<String>): Flow<List<Article>>

    suspend fun clearAllArticles(topics: List<String>)
}