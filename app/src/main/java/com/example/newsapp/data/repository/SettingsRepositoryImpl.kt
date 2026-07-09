package com.example.newsapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newsapp.data.mapper.toInterval
import com.example.newsapp.domain.entity.Language
import com.example.newsapp.domain.entity.Settings
import com.example.newsapp.domain.repository.SettingsRepository
import com.example.newsapp.domain.usecase.settings.GetSettingsUseCase
import com.example.newsapp.domain.usecase.settings.UpdateIntervalUseCase
import com.example.newsapp.domain.usecase.settings.UpdateLanguageUseCase
import com.example.newsapp.domain.usecase.settings.UpdateNotificationsEnabledUseCase
import com.example.newsapp.domain.usecase.settings.UpdateWifiOnlyUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase,
    @param:ApplicationContext private val context: Context
) : SettingsRepository {

    private val languageKey = stringPreferencesKey("language")
    private val intervalKey = intPreferencesKey("interval")
    private val notificationsEnabledKey = booleanPreferencesKey("notifications_enabled")
    private val wifiOnlyKey = booleanPreferencesKey("wifi_only")

    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map { preferences ->
            val languageAsString = preferences[languageKey] ?: Settings.DEFAULT_LANGUAGE.name
            val language = Language.valueOf(languageAsString)
            val interval = preferences[intervalKey]?.toInterval() ?: Settings.DEFAULT_INTERVAL
            val notificationsEnabled =
                preferences[notificationsEnabledKey] ?: Settings.DEFAULT_NOTIFICATIONS_ENABLED
            val wifiOnly = preferences[wifiOnlyKey] ?: Settings.DEFAULT_WIFI_ONLY

            Settings(
                language = language,
                interval = interval,
                notificationEnabled = notificationsEnabled,
                wifiOnly = wifiOnly
            )
        }
    }

    override suspend fun updateLanguage(language: Language) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language.name
        }
    }

    override suspend fun updateInterval(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[intervalKey] = minutes
        }
    }

    override suspend fun updateNotificationsEnabled(notificationsEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[notificationsEnabledKey] = notificationsEnabled
        }
    }

    override suspend fun updateWifiOnly(wifiOnly: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[wifiOnlyKey] = wifiOnly
        }
    }
}