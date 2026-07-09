package com.example.newsapp.domain.usecase.settings

import com.example.newsapp.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(enabled: Boolean) =
        settingsRepository.updateNotificationsEnabled(enabled)
}