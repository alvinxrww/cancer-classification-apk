package com.dicoding.asclepius.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.database.History
import com.dicoding.asclepius.data.local.database.HistoryDao
import com.dicoding.asclepius.data.local.database.HistoryRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application): ViewModel() {
    private val mHistoryDao: HistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = HistoryRoomDatabase.getDatabase(application)
        mHistoryDao = db.historyDao()
    }
    fun getAllHistory(): LiveData<List<History>> = mHistoryDao.getAllHistory()
    fun insert(history: History) {
        executorService.execute { mHistoryDao.insert(history) }
    }
}