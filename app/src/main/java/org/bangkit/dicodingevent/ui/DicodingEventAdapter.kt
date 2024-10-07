package org.bangkit.dicodingevent.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bangkit.dicodingevent.data.response.DicodingEvent
import org.bangkit.dicodingevent.databinding.ItemEventBinding

class DicodingEventAdapter(
    private val onClickItemListener : (DicodingEvent) -> Unit
) : ListAdapter<DicodingEvent, DicodingEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onClickItemListener)
    }

    class MyViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: DicodingEvent, onItemClickListener: (DicodingEvent) -> Unit) {
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.ivEventPicture)
            binding.tvEventTitle.text = event.name

            itemView.setOnClickListener {
                onItemClickListener(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DicodingEvent>() {
            override fun areItemsTheSame(oldItem: DicodingEvent, newItem: DicodingEvent): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DicodingEvent, newItem: DicodingEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}