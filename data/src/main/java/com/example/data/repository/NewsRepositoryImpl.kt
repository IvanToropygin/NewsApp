package com.example.data.repository

import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.data.background.RefreshDataWorker
import com.example.data.local.ArticleDbModel
import com.example.data.local.NewsDao
import com.example.data.local.SubscriptionDbModel
import com.example.data.mapper.toDbModels
import com.example.data.mapper.toEntities
import com.example.data.mapper.toQueryParam
import com.example.data.remote.NewsApiService
import com.example.domain.entity.Article
import com.example.domain.entity.Language
import com.example.domain.entity.RefreshConfig
import com.example.domain.repository.NewsRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService,
    private val workManager: WorkManager,
) : NewsRepository {

    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { subscriptions ->
            subscriptions.map { it.topic }
        }
    }

    override suspend fun addSubscription(topic: String) {
        newsDao.addSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForTopic(topic: String, language: Language): Boolean {
        val articles = loadArticles(topic, language)
        val ids = newsDao.addArticles(articles)
        return ids.any { it != -1L }
    }

    override suspend fun removeSubscriptions(topic: String) {
        newsDao.deleteSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForAllSubscriptions(language: Language): List<String> {
        val updatedTopics = mutableListOf<String>()
        val subscriptions = newsDao.getAllSubscriptions().first()
        coroutineScope {
            subscriptions.forEach {
                launch {
                    val updated = updateArticlesForTopic(it.topic, language)
                    if (updated) {
                        updatedTopics.add(it.topic)
                    }
                }
            }
        }
        return updatedTopics
    }

    override fun getArticlesByTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopics(topics).map { it.toEntities() }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        newsDao.deleteArticlesByTopics(topics)
    }

    private suspend fun loadArticles(
        topic: String,
        language: Language
    ): List<ArticleDbModel> {
        return try {
            newsApiService.loadArticles(topic, language.toQueryParam()).toDbModels(topic)
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("NewsRepository", "Error loading subscriptions", e)
            emptyList()
        }
    }

    override suspend fun loadTopHeadlines(): List<Article> {
        return try {
            newsApiService.loadTopArticles().toEntities()
        } catch (e: Exception) {
            if (e is CancellationException) {
                throw e
            }
            Log.e("NewsRepository", "Error loading top headlines", e)
            emptyList()
        }
    }

    override fun startBackgroundRefresh(refreshConfig: RefreshConfig) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                if (refreshConfig.wifiOnly) {
                    NetworkType.UNMETERED
                } else NetworkType.CONNECTED
            )
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            refreshConfig.interval.minutes.toLong(),
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "Refresh data",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request = request
        )
    }
}