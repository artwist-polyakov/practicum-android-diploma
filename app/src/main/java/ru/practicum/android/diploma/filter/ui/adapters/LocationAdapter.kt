package ru.practicum.android.diploma.filter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.databinding.LocationItemBinding
import ru.practicum.android.diploma.search.domain.models.SingleTreeElement

class LocationAdapter(
    private val onClick: (SingleTreeElement) -> Unit
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private var items = listOf<SingleTreeElement>()

    inner class LocationViewHolder(
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

    inner class LocationDiffCallback(
        private val oldList: List<SingleTreeElement>,
        private val newList: List<SingleTreeElement>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

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

    fun submitList(newItems: List<SingleTreeElement>) {
        val oldList = items
        CoroutineScope(Dispatchers.Default).launch {
            val diffResult = DiffUtil.calculateDiff(LocationDiffCallback(oldList, newItems))

            withContext(Dispatchers.Main) {
                items = newItems
                diffResult.dispatchUpdatesTo(this@LocationAdapter)
            }
        }
    }
}
