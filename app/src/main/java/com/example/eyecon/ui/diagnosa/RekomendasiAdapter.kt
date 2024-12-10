package com.example.eyecon.ui.diagnosa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eyecon.data.news.dataclass.Rekomendasi
import com.example.eyecon.databinding.ItemRekomendasiBinding

class RekomendasiAdapter : ListAdapter<Rekomendasi, RekomendasiAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRekomendasiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRekomendasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rekomendasi: Rekomendasi) {
            binding.apply {
                iconView.setImageResource(rekomendasi.icon)
                titleText.text = rekomendasi.title
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Rekomendasi>() {
            override fun areItemsTheSame(oldItem: Rekomendasi, newItem: Rekomendasi): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Rekomendasi, newItem: Rekomendasi): Boolean {
                return oldItem == newItem
            }
        }
    }
}