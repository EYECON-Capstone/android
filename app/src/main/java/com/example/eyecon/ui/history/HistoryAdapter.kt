package com.example.eyecon.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eyecon.data.photo.local.entity.HistoryEntity
import com.example.eyecon.databinding.ItemHistoryBinding
import com.example.eyecon.ui.history.HistoryResultActivity.Companion.EXTRA_ID

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(diffCallback) {
    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {

            binding.tvItemResult.text = history.result
            binding.tvItemCreatedat.text = history.createdAt
            Glide.with(itemView.context)
                .load(history.imgUrl)
                .into(binding.imgItemPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, HistoryResultActivity::class.java)
                intent.putExtra(EXTRA_ID, history.id)
                itemView.context.startActivity(intent)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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