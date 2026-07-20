package com.example.domain.usecase.news

import com.example.domain.repository.NewsRepository
import com.example.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic)
        CoroutineScope(currentCoroutineContext()).launch {
            val language = settingsRepository.getSettings().first().language
            newsRepository.updateArticlesForTopic(topic, language)
        }
    }
}