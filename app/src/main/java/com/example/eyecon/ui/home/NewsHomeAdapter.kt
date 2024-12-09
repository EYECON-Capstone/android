package com.example.eyecon.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eyecon.databinding.ItemHomeNewsBinding
import com.example.eyecon.data.news.dataclass.Article
import java.text.SimpleDateFormat
import java.util.*

class NewsHomeAdapter(
    private val onArticleClicked: (Article) -> Unit
) : ListAdapter<Article, NewsHomeAdapter.NewsViewHolder>(NewsDiffCallback()) {

    class NewsViewHolder(
        private val binding: ItemHomeNewsBinding,
        private val onArticleClicked: (Article) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.apply {
                textTitle.text = article.title ?: "Untitled"
                textReadTime.text = article.getReadTime()
                textTimePosted.text = article.getPostedTime()

                playButton.setOnClickListener {
                    article.url?.let { url ->
                        onArticleClicked(article)
                    }
                }
            }
        }

        private fun getFormattedTimeAgo(dateString: String): String {
            try {
                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                format.timeZone = TimeZone.getTimeZone("UTC")
                val date: Date = format.parse(dateString) ?: return "Unknown"
                val now: Long = System.currentTimeMillis()
                val diff: Long = now - date.time

                return when {
                    diff < 60_000 -> "Just now"
                    diff < 3600_000 -> "${diff / 60_000} minutes ago"
                    diff < 86400_000 -> "${diff / 3600_000} hours ago"
                    diff < 604800_000 -> "${diff / 86400_000} days ago"
                    else -> "${diff / 604800_000} weeks ago"
                }
            } catch (e: Exception) {
                return "Unknown"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemHomeNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, onArticleClicked)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class NewsDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}