package com.example.newsapp.domain.usecase.settings

import com.example.newsapp.domain.entity.Interval
import com.example.newsapp.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(interval: Interval) =
        settingsRepository.updateInterval(interval.minutes)
}