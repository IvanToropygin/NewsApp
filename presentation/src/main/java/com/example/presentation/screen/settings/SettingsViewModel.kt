package com.example.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.Interval
import com.example.domain.entity.Language
import com.example.domain.usecase.settings.GetSettingsUseCase
import com.example.domain.usecase.settings.UpdateIntervalUseCase
import com.example.domain.usecase.settings.UpdateLanguageUseCase
import com.example.domain.usecase.settings.UpdateNotificationsEnabledUseCase
import com.example.domain.usecase.settings.UpdateWifiOnlyUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettingsUseCase: GetSettingsUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SettingState>(SettingState.Initial)
    val state = _state.asStateFlow()

    init {
        getSettingsUseCase()
            .onEach { settings ->
                _state.update {
                    SettingState.Configuration(
                        language = settings.language,
                        interval = settings.interval,
                        notificationsEnabled = settings.notificationEnabled,
                        wifiOnly = settings.wifiOnly
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingsCommand.SelectInterval -> updateIntervalUseCase(command.interval)
                is SettingsCommand.SelectLanguage -> updateLanguageUseCase(command.language)
                is SettingsCommand.SwitchNotifications -> updateNotificationsEnabledUseCase(command.enabled)
                is SettingsCommand.SwitchWifiOnly -> updateWifiOnlyUseCase(command.wifiOnly)
            }
        }
    }
}

sealed interface SettingState {

    data object Initial : SettingState
    data class Configuration(
        val language: Language,
        val interval: Interval,
        val notificationsEnabled: Boolean,
        val wifiOnly: Boolean,
        val languages: List<Language> = Language.entries,
        val intervals: List<Interval> = Interval.entries
    ) : SettingState
}

sealed interface SettingsCommand {

    data class SelectLanguage(val language: Language) : SettingsCommand
    data class SelectInterval(val interval: Interval) : SettingsCommand
    data class SwitchNotifications(val enabled: Boolean) : SettingsCommand
    data class SwitchWifiOnly(val wifiOnly: Boolean) : SettingsCommand
}