package com.example.newsapp.domain.usecase.settings

import com.example.newsapp.domain.entity.Language
import com.example.newsapp.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(language: Language) =
        settingsRepository.updateLanguage(language)
}