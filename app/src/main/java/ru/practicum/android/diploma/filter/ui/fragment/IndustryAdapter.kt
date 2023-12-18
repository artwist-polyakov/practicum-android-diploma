package ru.practicum.android.diploma.filter.ui.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryListItemBinding
import ru.practicum.android.diploma.search.domain.models.Industry

class IndustryAdapter (
    private val clickListener: (Industry) -> Unit
) : RecyclerView.Adapter<IndustryAdapter.IndustryViewHolder>() {

    var selectedView: RadioButton? = null
    var selectedPosition: Int? = null

    inner class IndustryViewHolder(
        private val binding: IndustryListItemBinding,
        private val clickListener: (Industry) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Industry) = with(binding) {
            binding.name.text = data.name
            with(binding.radioButton) {
                isChecked = adapterPosition == selectedPosition
                setOnClickListener {
                    itemView.callOnClick()
                }
            }
            itemView.setOnClickListener {
                selectedView?.let{
                    it.isChecked = false
                    notifyItemChanged(selectedPosition!!)
                }

                selectedView = binding.radioButton
                selectedPosition = adapterPosition
                selectedView?.let{
                    it.isChecked = true
                    notifyItemChanged(selectedPosition!!)
                }
                clickListener(dataList[selectedPosition!!])
            }
        }
    }

    private var dataList = ArrayList<Industry>()

    fun setData(data: List<Industry>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IndustryListItemBinding.inflate(layoutInflater, parent, false)
        return IndustryViewHolder(binding, clickListener)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

}
