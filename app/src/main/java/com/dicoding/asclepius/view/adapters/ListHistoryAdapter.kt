package com.dicoding.asclepius.view.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.ItemRowHistoryBinding

class ListHistoryAdapter(private val onItemClick: (History) -> Unit) : ListAdapter<History, ListHistoryAdapter.ListViewHolder>(
    DIFF_CALLBACK
) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(
                oldItem: History,
                newItem: History
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: History,
                newItem: History
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ListViewHolder(private val binding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(history: History, onItemClick: (History) -> Unit) {
                val imageUri = Uri.parse(history.image)
                Glide.with(binding.cardImage.context)
                    .load(imageUri)
                    .into(binding.cardImage)
                binding.cardTitle.text = history.result
                binding.deleteButton.setOnClickListener {
                    onItemClick(history)
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history, onItemClick)
    }
}