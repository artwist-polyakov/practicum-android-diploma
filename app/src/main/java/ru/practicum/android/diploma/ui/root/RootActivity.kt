package ru.practicum.android.diploma.ui.root

// import ru.practicum.android.diploma.BuildConfig
import com.hellcorp.selfdictation.utils.BaseActivity
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : BaseActivity<ActivityRootBinding>(ActivityRootBinding::inflate) {

    override fun initViews() {
        // Пример использования access token для HeadHunter API
        // networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }
}
