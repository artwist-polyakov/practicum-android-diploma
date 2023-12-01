package ru.practicum.android.diploma.favorite

import androidx.fragment.app.viewModels
import com.hellcorp.selfdictation.utils.BaseFragment
import ru.practicum.android.diploma.databinding.FragmentFavoriteBinding

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding, FavoriteViewModel>(FragmentFavoriteBinding::inflate) {
    override val viewModel: FavoriteViewModel by viewModels()

    override fun initViews() {
    }

    override fun subscribe() {
    }
}
