package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyListItemBinding
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class VacancyAdapter(private val clickListener: (VacancyGeneral) -> Unit) :
    RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {

    class VacancyViewHolder(
        private val binding: VacancyListItemBinding,
        private val clickListener: (VacancyGeneral) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val radius = itemView.resources.getDimension(R.dimen.vacancy_logo_corner_radius)
        fun bind(data: VacancyGeneral) = with(binding) {
            vacancyName.text = data.title
            vacancySalary.text = parseSalary(
                data.salaryFrom, data.salaryTo, data.salaryCurrency
            )
            companyName.text = data.employerName
            companyImage.load(data.employerLogo) {
                placeholder(R.drawable.placeholder_48px)
                error(R.drawable.placeholder_48px)
            }
            val shapeAppearanceModel = companyImage.shapeAppearanceModel.toBuilder().setAllCornerSizes(radius).build()
            companyImage.shapeAppearanceModel = shapeAppearanceModel

            itemView.setOnClickListener { clickListener(data) }
        }

        private fun formatSalary(value: Int): String {
            val decimalFormat = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))
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

    private var dataList = ArrayList<VacancyGeneral>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = VacancyListItemBinding.inflate(layoutInflater, parent, false)
        return VacancyViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(data: List<VacancyGeneral>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: List<VacancyGeneral>) {
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }
}
