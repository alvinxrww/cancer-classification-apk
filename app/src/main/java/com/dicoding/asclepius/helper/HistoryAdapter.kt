package com.dicoding.asclepius.helper

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.data.local.database.History
import com.dicoding.asclepius.databinding.HistoryItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoryAdapter : ListAdapter<History, HistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val historyBinding = holder.binding
        val history = getItem(position)

        val imageUri = Uri.parse(history.image)
        val label = history.label
        val score = history.score
        val date = history.date

        val currentDateTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedNow = formatter.format(currentDateTime)
        val datetimeDiff = getDateDiff(date ?: " ", formattedNow)

        historyBinding.imgItemPhoto.setImageURI(imageUri)
        historyBinding.tvItemLabel.text = label
        historyBinding.tvItemScore.text = score
        historyBinding.tvItemDatetime.text = datetimeDiff
    }

    private fun getDateDiff(date1: String, date2: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate1 = sdf.parse(date1) ?: Calendar.getInstance().time
        val formattedDate2 = sdf.parse(date2) ?: Calendar.getInstance().time

        val diffInMillis = formattedDate2.time - formattedDate1.time
        val diffSeconds = diffInMillis / 1000

        if (diffSeconds < 60) {
            return when (diffSeconds) {
                1L -> "$diffSeconds second ago"
                else -> "$diffSeconds seconds ago"
            }
        }

        val diffMinutes = diffSeconds / 60

        if (diffMinutes < 60) {
            return when (diffMinutes) {
                1L -> "$diffMinutes minute ago"
                else -> "$diffMinutes minutes ago"
            }
        }

        // Calculating the difference in hours
        val diffHours = diffMinutes / 60

        // If difference is less than a day, return hours ago
        if (diffHours < 24) {
            return when (diffHours) {
                1L -> "$diffHours hour ago"
                else -> "$diffHours hours ago"
            }
        }

        // Otherwise, return days ago
        val diffDays = diffHours / 24
        return when (diffDays) {
            1L -> "$diffDays day ago"
            else -> "$diffDays days ago"
        }
    }

    class ListViewHolder(var binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}