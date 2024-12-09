package com.example.eyecon.ui.diagnosa

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.eyecon.data.news.dataclass.Diagnosa
import com.example.eyecon.databinding.ItemDiagnosaBinding


class DiagnosaAdapter(
    private val onItemClick: (Diagnosa) -> Unit
) : ListAdapter<Diagnosa, DiagnosaAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDiagnosaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemDiagnosaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(diagnosa: Diagnosa) {
            binding.apply {
                imageView.setImageResource(diagnosa.imageResId)
                titleText.text = diagnosa.title
                root.setOnClickListener { onItemClick(diagnosa) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Diagnosa>() {
        override fun areItemsTheSame(oldItem: Diagnosa, newItem: Diagnosa) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Diagnosa, newItem: Diagnosa) =
            oldItem == newItem
    }
}