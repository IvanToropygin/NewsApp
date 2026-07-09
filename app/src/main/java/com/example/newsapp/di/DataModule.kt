package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.local.NewsDatabase
import com.example.newsapp.data.remote.NewsApiService
import com.example.newsapp.data.repository.NewsRepositoryImpl
import com.example.newsapp.data.repository.SettingsRepositoryImpl
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.domain.repository.SettingsRepository
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
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

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
            return retrofit.create()
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

        @Singleton
        @Provides
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}