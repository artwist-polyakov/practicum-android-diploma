package ru.practicum.android.diploma.search.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.utils.formatSalary
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

class VacancyAdapter(
    private val clickListener: (VacancyGeneral) -> Unit,
    private val longClickListener: (VacancyGeneral) -> Boolean
) : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    inner class VacancyViewHolder(
        private val binding: VacancyListItemBinding,
        private val clickListener: (VacancyGeneral) -> Unit,
        private val longClickListener: (VacancyGeneral) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        private val radius = itemView.resources.getDimension(R.dimen.vacancy_logo_corner_radius)
        fun bind(data: VacancyGeneral) = with(binding) {
            companyImage.load(data.employerLogo) {
                placeholder(R.drawable.placeholder_48px)
                error(R.drawable.placeholder_48px)
            }
            companyName.text = data.employerName
            val titleBuilder: StringBuilder = StringBuilder()
            titleBuilder.append(data.title)
            data.region?.let {
                titleBuilder.append(", ")
                titleBuilder.append(it)
            }
            vacancyName.text = titleBuilder.toString()
            vacancySalary.text = parseSalary(
                data.salaryFrom, data.salaryTo, data.salaryCurrency
            )
            val shapeAppearanceModel = companyImage.shapeAppearanceModel.toBuilder().setAllCornerSizes(radius).build()
            companyImage.shapeAppearanceModel = shapeAppearanceModel

            itemView.setOnClickListener { clickListener(data) }
            itemView.setOnLongClickListener { longClickListener(data) }
        }

        fun showLoadingIndicator() {
            binding.pbLoadingBar.visibility = View.VISIBLE
        }

        fun hideLoadingIndicator() {
            binding.pbLoadingBar.visibility = View.GONE
        }

        private fun parseSalary(from: Int?, to: Int?, currency: String?): String {
            val usedCurrency = currency ?: ""
            return when {
                from != null && to != null ->
                    itemView.resources.getString(
                        R.string.salary_from_to,
                        from.formatSalary(),
                        to.formatSalary(),
                        usedCurrency
                    )

                from != null ->
                    itemView.resources.getString(R.string.salary_from, from.formatSalary(), usedCurrency)

                to != null ->
                    itemView.resources.getString(R.string.salary_to, to.formatSalary(), usedCurrency)

                else ->
                    itemView.resources.getString(R.string.salary_not_specified)
            }
        }
    }

    private var currentPage: Int = 0
    private var dataList = ArrayList<VacancyGeneral>()
    private var showScrollLoading = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VacancyListItemBinding.inflate(layoutInflater, parent, false)
        return VacancyViewHolder(binding, clickListener, longClickListener)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(dataList[position])
        if (showScrollLoading && position == dataList.size - 1) {
            holder.showLoadingIndicator()
        } else {
            holder.hideLoadingIndicator()
        }

    }

    override fun getItemCount(): Int = dataList.size

    fun setData(data: List<VacancyGeneral>, pageNum: Int = 0) {
        val diffCallback = VacancyDiffCallback(dataList, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        dataList.clear()
        dataList.addAll(data)
        currentPage = pageNum
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearPageCounter() {
        currentPage = 0
    }

    fun setScrollLoadingEnabled(show: Boolean) {
        showScrollLoading = show
    }

    fun refreshLastItem() {
        notifyItemChanged(dataList.size - 1)
    }
}
