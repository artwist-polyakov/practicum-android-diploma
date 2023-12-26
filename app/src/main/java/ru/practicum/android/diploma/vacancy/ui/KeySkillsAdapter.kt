package ru.practicum.android.diploma.vacancy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.KeySkillItemBinding

class KeySkillsAdapter(private val keySkills: List<String>) :
    RecyclerView.Adapter<KeySkillsAdapter.KeySkillsViewHolder>() {
    class KeySkillsViewHolder(val binding: KeySkillItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeySkillsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = KeySkillItemBinding.inflate(layoutInflater, parent, false)
        return KeySkillsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeySkillsViewHolder, position: Int) {
        holder.binding.textViewSkill.text = keySkills[position]
    }

    override fun getItemCount() = keySkills.size
}
