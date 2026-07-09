package com.example.newsapp.domain.usecase.news

import com.example.newsapp.domain.entity.Article
import com.example.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetArticlesByTopicsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    operator fun invoke(topics: List<String>): Flow<List<Article>> =
        newsRepository.getArticlesByTopics(topics)
}