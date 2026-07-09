package com.example.newsapp.data.backgroud

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.newsapp.domain.usecase.UpdateSubscribedArticleUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateSubscribedArticleUseCase: UpdateSubscribedArticleUseCase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "start")
        updateSubscribedArticleUseCase
        Log.d("RefreshDataWorker", "finish")
        return Result.success()
    }
}