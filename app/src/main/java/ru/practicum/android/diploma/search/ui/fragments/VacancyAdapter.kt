package ru.practicum.android.diploma.search.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.VacancyGeneral

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

    private var companyLogo: ImageView = itemView.findViewById(R.id.company_image)
    private var companyName: TextView = itemView.findViewById(R.id.company_name)
    private var vacancyTitle: TextView = itemView.findViewById(R.id.vacancy_name)
    private var vacancySalary: TextView = itemView.findViewById(R.id.vacancy_salary)
    fun bind(data: VacancyGeneral) {
//        Glide.with(itemView)
//            .load(data.employerLogo)
//            .into(companyLogo)

        vacancyTitle.text = data.title
        companyName.text = data.employerName
        vacancySalary.text = parseSalary(data)

        itemView.setOnClickListener { clickListener.onClick(data) }
    }

    fun parseSalary(data: VacancyGeneral): String {
        var list: MutableList<String> = mutableListOf()
        data.salaryFrom?.let {
            list.add(itemView.resources.getString(R.string.salary_from, it.toString()))
        }
        data.salaryTo?.let {
            list.add(itemView.resources.getString(R.string.salary_to, it.toString()))
        }
        if (list.isEmpty()) {
            list.add(itemView.resources.getString(R.string.salary_not_specified))
        } else {
            data.salaryCurrency?.let { list.add(data.salaryCurrency) }
        }
        return list.joinToString(" ")
    }
}
