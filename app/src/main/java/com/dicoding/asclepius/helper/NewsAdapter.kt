package com.dicoding.asclepius.helper

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.databinding.NewsItemBinding

class NewsAdapter : ListAdapter<ArticlesItem, NewsAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val news = getItem(position)
        val imageUrl = news.urlToImage
        val title = news.title
        val description = news.description

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.binding.imgItemPhoto)
        holder.binding.tvItemTitle.text = title
        holder.binding.tvItemDescription.text = description

        holder.itemView.setOnClickListener {
            val newsUrl = news.url
            val context = holder.itemView.context
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl))
            context.startActivity(intent)
        }
    }

    class ListViewHolder(var binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}