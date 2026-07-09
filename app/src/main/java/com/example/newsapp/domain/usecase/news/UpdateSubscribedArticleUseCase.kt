package com.example.newsapp.domain.usecase.news

import com.example.newsapp.domain.repository.NewsRepository
import javax.inject.Inject

class UpdateSubscribedArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke() {
        return newsRepository.updateArticlesForAllSubscriptions()
    }
}