package com.example.newsapp.domain.usecase.settings

import com.example.newsapp.data.mapper.toRefreshConfig
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StartRefreshDataUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke() {
        settingsRepository.getSettings()
            .map { it.toRefreshConfig() }
            .distinctUntilChanged()
            .onEach {
                newsRepository.startBackgroundRefresh(it)
            }
            .collect()
    }
}