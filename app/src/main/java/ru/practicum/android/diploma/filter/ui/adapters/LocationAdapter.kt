package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.LocationItemBinding
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement

class LocationAdapter(
    private val items: List<SingleTreeElement>,
    private val onClick: (SingleTreeElement) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = LocationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocationViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    class LocationViewHolder(
        private val binding: LocationItemBinding,
        private val onClick: (SingleTreeElement) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SingleTreeElement) {
            binding.apply {
                tvName.text = item.name
                root.setOnClickListener { onClick(item) }
            }
        }
    }
}

