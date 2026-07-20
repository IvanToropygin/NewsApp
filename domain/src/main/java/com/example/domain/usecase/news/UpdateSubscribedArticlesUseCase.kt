package com.example.domain.usecase.news

import com.example.domain.repository.NewsRepository
import com.example.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(): List<String> {
        val language = settingsRepository.getSettings().first().language
        return newsRepository.updateArticlesForAllSubscriptions(language)
    }
}