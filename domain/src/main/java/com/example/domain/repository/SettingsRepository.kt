package com.example.domain.repository

import com.example.domain.entity.Language
import com.example.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)

    suspend fun updateInterval(minutes: Int)

    suspend fun updateNotificationsEnabled(notificationsEnabled: Boolean)

    suspend fun updateWifiOnly(wifiOnly: Boolean)
}