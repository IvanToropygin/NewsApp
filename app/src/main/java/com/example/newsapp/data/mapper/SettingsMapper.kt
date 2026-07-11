package com.example.newsapp.data.mapper

import com.example.newsapp.domain.entity.RefreshConfig
import com.example.newsapp.domain.entity.Settings

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(
        language = language,
        interval = interval,
        wifiOnly = wifiOnly
    )
}