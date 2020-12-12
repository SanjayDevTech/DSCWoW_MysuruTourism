package com.sanjaydevtech.mysurutourism

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DownloadWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    private val db = Firebase.firestore
    override suspend fun doWork(): Result {

        return Result.success()
    }
}