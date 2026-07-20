package com.example.domain.usecase.news

import com.example.domain.repository.NewsRepository
import javax.inject.Inject

class ClearAllArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topics: List<String>) = newsRepository.clearAllArticles(topics)
}