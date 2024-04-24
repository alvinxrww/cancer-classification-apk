package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.local.database.History
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.helper.HistoryAdapter
import com.dicoding.asclepius.helper.HistoryViewModel

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        historyViewModel = HistoryViewModel(this.application)
        val adapter = HistoryAdapter()
        historyViewModel.getAllHistory().observe(this) { histories ->
            val items = arrayListOf<History>()
            histories.map {
                val item = History(
                    image = it.image,
                    label = it.label,
                    score = it.score,
                    date = it.date
                )
                items.add(item)
            }
            adapter.submitList(items)
            binding.rvHistory.adapter = adapter
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }
}