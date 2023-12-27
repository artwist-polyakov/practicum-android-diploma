package ru.practicum.android.diploma.root.ui

import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseActivity
import ru.practicum.android.diploma.common.ui.MainActivityBlur
import ru.practicum.android.diploma.common.utils.applyBlurEffect
import ru.practicum.android.diploma.common.utils.clearBlurEffect
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import java.util.Locale

@AndroidEntryPoint
class RootActivity : BaseActivity<ActivityRootBinding>(ActivityRootBinding::inflate), MainActivityBlur {

    override fun initViews() {
        manageBottomNavigation()
        setStatusBarColor()
        setBlurView()
    }

    /**
     * Метод управляет видимостью элементов и назначением текста на Header
     * При добавлении новых фрагментов следует вписать его в сценарий manageBottomNavigation
     */
    private fun manageBottomNavigation() = with(binding) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment, R.id.favoriteFragment, R.id.teamFragment -> bottomNavigationView.visibility =
                    View.VISIBLE

                else -> bottomNavigationView.visibility = View.GONE
            }
        }
    }

    /**
     * Метод определяет теккущую тему устройства (тёмная/светлая) и относительно этого устанавливает цвета в статус баре
     */
    private fun setStatusBarColor() {
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (uiMode == Configuration.UI_MODE_NIGHT_NO) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.whiteDay)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            window.statusBarColor = ContextCompat.getColor(this, R.color.blackDay)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                window.decorView.systemUiVisibility = 0
            }
        }
    }

    private fun setBlurView() = with(binding) {
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this@RootActivity, R.color.transparent))
        val radius = BLUR_RADIUS
        val decorView = window.decorView
        val rootView = decorView.findViewById(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background

        blurview.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
    }

    override fun applyBlurEffect() {
        binding.bottomNavigationView.applyBlurEffect()
    }

    override fun clearBlurEffect() {
        binding.bottomNavigationView.clearBlurEffect()
    }

    companion object {
        private const val BLUR_RADIUS = 20f
    }
}
