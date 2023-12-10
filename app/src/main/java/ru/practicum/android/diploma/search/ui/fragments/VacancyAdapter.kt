package ru.practicum.android.diploma.search.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale


class VacancyAdapter(
    private val scrollController: ListScrollListener,
    private val clickListener: VacancyClickListener
) : RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    private var currentPage: Int = 0
    private var dataList = ArrayList<VacancyGeneral>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VacancyListItemBinding.inflate(inflater, parent, false)
        return VacancyViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        if (position == dataList.size - LOADING_THRESHOLD) {
            scrollController.onScrollToBottom(currentPage + 1)
        }
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class VacancyViewHolder(
        private val binding: VacancyListItemBinding,
        private val clickListener: VacancyClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val radius = itemView.resources.getDimension(R.dimen.vacancy_logo_corner_radius)

        fun bind(data: VacancyGeneral) {
            with(binding) {
                companyImage.load(data.employerLogo) {
                    placeholder(R.drawable.placeholder_48px)
                    error(R.drawable.placeholder_48px)
                }
                companyName.text = data.employerName
                vacancyName.text = data.title
                vacancySalary.text = parseSalary(data.salaryFrom, data.salaryTo, data.salaryCurrency)

                val shapeAppearanceModel = companyImage.shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(radius)
                    .build()
                companyImage.shapeAppearanceModel = shapeAppearanceModel

                root.setOnClickListener { clickListener.onClick(data) }
            }
        }

        private fun formatSalary(value: Int): String {
            val decimalFormat = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.GERMANY))
            return decimalFormat.format(value).replace(",", " ").toString()
        }

        private fun parseSalary(
            from: Int?,
            to: Int?,
            currency: String?
        ): String {
            val usedCurrency = currency ?: ""
            return if (from == null || to == null) {
                if (from != null) {
                    itemView.resources.getString(R.string.salary_from, formatSalary(from), usedCurrency)
                } else if (to != null) {
                    itemView.resources.getString(R.string.salary_to, formatSalary(to), usedCurrency)
                } else {
                    itemView.resources.getString(R.string.salary_not_specified)
                }
            } else {
                itemView.resources.getString(
                    R.string.salary_from_to,
                    formatSalary(from),
                    formatSalary(to),
                    usedCurrency
                )
            }
        }
    }

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

    interface VacancyClickListener {
        fun onClick(data: VacancyGeneral)
    }

    interface ListScrollListener {
        fun onScrollToBottom(nextPage: Int)
    }

    companion object {
        private const val LOADING_THRESHOLD = 3
    }
}
