package com.dicoding.asclepius.view

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.database.History
import com.dicoding.asclepius.data.local.repository.HistoryRepository

class HistoryViewModel(application: Application) {
    private val mHistoryRepository: HistoryRepository =
        HistoryRepository(application)

    fun getAllHistory(): LiveData<List<History>> =
        mHistoryRepository.getAllHistory()

    fun insert(history: History) {
        mHistoryRepository.insert(history)
    }
}