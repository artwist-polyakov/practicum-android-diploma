package ru.practicum.android.diploma.search.ui.fragments

import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

class VacancyDiffCallback(
    private val oldList: List<VacancyGeneral>,
    private val newList: List<VacancyGeneral>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
