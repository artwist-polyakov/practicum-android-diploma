package ru.practicum.android.diploma.root.ui

// import ru.practicum.android.diploma.BuildConfig
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseActivity
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : BaseActivity<ActivityRootBinding>(ActivityRootBinding::inflate) {

    override fun initViews() {
        // Пример использования access token для HeadHunter API
        // networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
        setStatusBarColor()
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

    /**
     *Метод определяет теккущую тему устройства (тёмная/светлая) и относительно этого устанавливает цвета в статус баре
     */
    private fun setStatusBarColor() {
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (uiMode == Configuration.UI_MODE_NIGHT_NO) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.decorView.systemUiVisibility = 0
            }
        }
    }
}
