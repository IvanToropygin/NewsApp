package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.NewsDao
import com.example.data.local.NewsDatabase
import com.example.data.remote.NewsApiService
import com.example.data.repository.NewsRepositoryImpl
import com.example.data.repository.SettingsRepositoryImpl
import com.example.domain.repository.NewsRepository
import com.example.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    companion object {

        @Singleton
        @Provides
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Singleton
        @Provides
        fun provideConverterFactory(json: Json): Converter.Factory {
            return json.asConverterFactory("application/json".toMediaType())
        }

        @Singleton
        @Provides
        fun provideRetrofit(converterFactory: Converter.Factory): Retrofit {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(converterFactory)
                .build()
            return retrofit
        }

        @Singleton
        @Provides
        fun provideApiService(retrofit: Retrofit): NewsApiService {
            return retrofit.create(NewsApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = NewsDatabase::class.java,
                name = "news.db"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        @Singleton
        @Provides
        fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao {
            return newsDatabase.newsDao()
        }
    }
}