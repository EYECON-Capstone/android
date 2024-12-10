package com.example.eyecon.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.databinding.ItemRowHistoryBinding
import com.example.eyecon.ui.history.HistoryResultActivity
import com.example.eyecon.ui.history.HistoryResultActivity.Companion.EXTRA_ID
import com.example.eyecon.ui.history.formatDate

class HistoryHorizontalAdapter : ListAdapter<HistoryEntity, HistoryHorizontalAdapter.HistoryViewHolder>(diffCallback) {
    class HistoryViewHolder(private val binding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            binding.title.text = history.result
            binding.createdat.text = formatDate(history.createdAt)
            Glide.with(itemView.context)
                .load(history.imgUrl)
                .transform(CircleCrop())
                .into(binding.image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, HistoryResultActivity::class.java)
                intent.putExtra(EXTRA_ID, history.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}