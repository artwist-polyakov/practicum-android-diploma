package ru.practicum.android.diploma.search.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class VacancyAdapter(private val clickListener: VacancyClickListener) :
    RecyclerView.Adapter<VacancyViewHolder>() {

    private var dataList = ArrayList<VacancyGeneral>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder =
        VacancyViewHolder(parent, clickListener)

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    interface VacancyClickListener {
        fun onClick(data: VacancyGeneral)
    }

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

class VacancyViewHolder(
    parent: ViewGroup,
    private val clickListener: VacancyAdapter.VacancyClickListener,
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.vacancy_list_item, parent, false)
    ) {

    private var companyLogo: ShapeableImageView = itemView.findViewById(R.id.company_image)
    private var companyName: TextView = itemView.findViewById(R.id.company_name)
    private var vacancyTitle: TextView = itemView.findViewById(R.id.vacancy_name)
    private var vacancySalary: TextView = itemView.findViewById(R.id.vacancy_salary)
    private val radius = itemView.resources.getDimension(R.dimen.vacancy_logo_corner_radius)
    fun bind(data: VacancyGeneral) {
        vacancyTitle.text = data.title
        vacancySalary.text = parseSalary(
            data.salaryFrom,
            data.salaryTo,
            data.salaryCurrency
        )
        companyName.text = data.employerName
        companyLogo.load(data.employerLogo) {
            placeholder(R.drawable.placeholder_48px)
            error(R.drawable.placeholder_48px)
        }
        val shapeAppearanceModel = companyLogo.shapeAppearanceModel.toBuilder()
            .setAllCornerSizes(radius)
            .build()
        companyLogo.shapeAppearanceModel = shapeAppearanceModel

        itemView.setOnClickListener { clickListener.onClick(data) }
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
