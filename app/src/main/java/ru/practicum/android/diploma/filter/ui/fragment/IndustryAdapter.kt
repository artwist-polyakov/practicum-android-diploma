package ru.practicum.android.diploma.filter.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding
import ru.practicum.android.diploma.search.domain.models.Industry

class IndustryAdapter(
    private val clickListener: (Industry) -> Unit
) : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {

    private var selectedId: String? = null
    private var filterText: String = ""

    inner class IndustryViewHolder(
        private val binding: IndustryListItemBinding,
        private val clickListener: (Industry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Industry) = with(binding) {
            name.text = data.name
            with(radioButton) {
                isChecked = data.id == selectedId
                setOnClickListener {
                    selectedId = data.id
                    clickListener(data)
                    notifyDataSetChanged()
                }
            }
            itemView.setOnClickListener {
                radioButton.callOnClick()
            }
        }
    }

    private var dataList: MutableList<Industry> = mutableListOf()
    private var filteredList: MutableList<Industry> = mutableListOf()

    fun setSelectedIndustry(id: String?) {
        selectedId = id
        filtrate()
    }

    fun setData(data: List<Industry>) {
        dataList.clear()
        dataList.addAll(data)
        filtrate()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IndustryListItemBinding.inflate(layoutInflater, parent, false)
        return IndustryViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    fun setFilter(text: String) {
        filterText = text
        filtrate()
    }

    private fun filtrate() {
        filteredList.clear()
        if (filterText.isEmpty()) {
            filteredList = dataList.toMutableList()
        } else {
            for (data in dataList) {
                if (data.name.lowercase().contains(filterText)) {
                    filteredList.add(data)
                }
            }
        }
        notifyDataSetChanged()
    }
}
