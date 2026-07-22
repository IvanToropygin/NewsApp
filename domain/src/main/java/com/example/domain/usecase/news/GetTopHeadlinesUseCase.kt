package com.example.domain.usecase.news

import com.example.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke() = newsRepository.loadTopHeadlines()
}