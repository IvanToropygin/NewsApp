package com.example.data.mapper

import com.example.data.local.ArticleDbModel
import com.example.data.remote.NewsResponseDto
import com.example.domain.entity.Article
import com.example.domain.entity.Interval
import com.example.domain.entity.Language
import java.text.SimpleDateFormat
import java.util.Locale

fun NewsResponseDto.toDbModels(topic: String): List<ArticleDbModel> {
    return articles.map {
        ArticleDbModel(
            title = it.title,
            description = it.description,
            imageUrl = it.urlToImage,
            sourceName = it.sourceDto.name,
            publishedAt = it.publishedAt.toTimestamp(),
            url = it.url,
            topic = topic
        )
    }
}

fun List<ArticleDbModel>.toEntities(): List<Article> {
    return map {
        Article(
            title = it.title,
            description = it.description,
            imageUrl = it.imageUrl,
            sourceName = it.sourceName,
            publishedAt = it.publishedAt,
            url = it.url
        )
    }.distinct()
}

private fun String.toTimestamp(): Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}

fun Int.toInterval(): Interval {
    return Interval.entries.first { it.minutes == this }
}

fun Language.toQueryParam(): String {
    return when (this) {
        Language.ENGLISH -> "en"
        Language.RUSSIAN -> "ru"
        Language.FRENCH -> "fr"
        Language.GERMAN -> "de"
    }
}