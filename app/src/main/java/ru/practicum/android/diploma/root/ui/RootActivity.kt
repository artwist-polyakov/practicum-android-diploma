package ru.practicum.android.diploma.root.ui

// import ru.practicum.android.diploma.BuildConfig
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.ui.BaseActivity
import ru.practicum.android.diploma.databinding.ActivityRootBinding

@AndroidEntryPoint
class RootActivity : BaseActivity<ActivityRootBinding>(ActivityRootBinding::inflate) {

    override fun initViews() = with(binding) {
        // Пример использования access token для HeadHunter API
        // networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)

        manageBottomNavigation()
        setStatusBarColor()
    }

//    private fun networkRequestExample(accessToken: String) {
//        // ...
//    }

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
                R.id.searchFragment -> manageItemViews(
                    headerText = getString(R.string.search_for_vacancies),
                    arrowBackIcon = false,
                    bottomNavigation = true,
                    shareIcon = false,
                    favoriteIcon = false,
                    filterIcon = true
                )

                R.id.favoriteFragment -> manageItemViews(
                    headerText = getString(R.string.favorite),
                    arrowBackIcon = false,
                    bottomNavigation = true,
                    shareIcon = false,
                    favoriteIcon = false,
                    filterIcon = false
                )

                R.id.teamFragment -> manageItemViews(
                    headerText = getString(R.string.team),
                    arrowBackIcon = false,
                    bottomNavigation = true,
                    shareIcon = false,
                    favoriteIcon = false,
                    filterIcon = false
                )

                R.id.filterFragment -> manageItemViews(
                    headerText = getString(R.string.filter_settings),
                    arrowBackIcon = true,
                    bottomNavigation = true,
                    shareIcon = false,
                    favoriteIcon = false,
                    filterIcon = false
                )

                else -> bottomNavigationView.visibility = View.VISIBLE
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

    /**
     * Метод-конструктор элементов RootActivity
     */
    private fun manageItemViews(
        headerText: String,
        arrowBackIcon: Boolean,
        bottomNavigation: Boolean,
        shareIcon: Boolean,
        favoriteIcon: Boolean,
        filterIcon: Boolean
    ) {
        with(binding) {
            ivArrowBack.isVisible = arrowBackIcon
            tvHeader.text = headerText
            bottomNavigationView.isVisible = bottomNavigation
            ivShare.isVisible = shareIcon
            ivFavorite.isVisible = favoriteIcon
            ivFilter.isVisible = filterIcon
        }
    }
}
