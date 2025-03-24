package com.example.sub2dicodingeventfun4.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sub2dicodingeventfun4.DetailActivity
import com.example.sub2dicodingeventfun4.data.local.entity.EventEntity
import com.example.sub2dicodingeventfun4.databinding.ItemEventBinding

class FavoriteEventAdapter : RecyclerView.Adapter<FavoriteEventAdapter.FavoriteEventViewHolder>() {

    private var favoriteEvents = listOf<EventEntity>()

    fun setFavoriteEvents(events: List<EventEntity>) {
        favoriteEvents = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteEventViewHolder, position: Int) {
        holder.bind(favoriteEvents[position])
    }

    override fun getItemCount(): Int = favoriteEvents.size

    inner class FavoriteEventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.eventName.text = event.name
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.eventPoster)
            itemView.setOnClickListener {
                event.id.let {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_EVENT, it)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}