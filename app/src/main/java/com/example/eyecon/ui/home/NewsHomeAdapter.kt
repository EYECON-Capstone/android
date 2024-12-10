package com.example.eyecon.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eyecon.databinding.ItemHomeNewsBinding
import com.example.eyecon.data.news.dataclass.Article

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