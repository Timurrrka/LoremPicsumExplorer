package ru.musintimur.lorempicsumexplorer.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.musintimur.lorempicsumexplorer.R
import ru.musintimur.lorempicsumexplorer.app.MainApp
import ru.musintimur.lorempicsumexplorer.app.preferences.IntProperties
import ru.musintimur.lorempicsumexplorer.utils.checkExceptionType

class MainActivity : AppCompatActivity(),
    MainContract,
    ThrowableCallback {

    private var onSettingsItemClick: (() -> Unit)? = null
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> navController.popBackStack()
            R.id.itemSettings -> onSettingsItemClick?.invoke()
        }
        return true
    }

    private fun getDisplayDimension(): Pair<Int, Int> {
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)
        return Pair(metrics.widthPixels, metrics.heightPixels)
    }

    override fun calculateColumns(): Int {
        val photoWidth = MainApp.preferences
            .getInt(
                IntProperties.PHOTO_WIDTH.propertyName,
                IntProperties.PHOTO_WIDTH.defaultValue
            )
        val screenWidth = getDisplayDimension().first
        val columns = screenWidth / photoWidth
        return if (columns < 1) 1 else columns
    }

    override fun setupProgressBarVisibility(visibility: MainContract.ViewVisibility) {
        mainProgressBar.visibility = when(visibility) {
            MainContract.ViewVisibility.VISIBLE -> View.VISIBLE
            MainContract.ViewVisibility.GONE -> View.GONE
        }
    }

    override fun setOnSettingsItemClick(action: () -> Unit) {
        onSettingsItemClick = action
    }

    override fun onSuccess() {
        CoroutineScope(Dispatchers.Main).launch {
            layoutFragments.visibility = View.VISIBLE
            (includeError as TextView).run {
                visibility = View.GONE
                text = null
            }
        }
    }

    override fun onError(e: Throwable) {
        val errorMessage = checkExceptionType(e)
        CoroutineScope(Dispatchers.Main).launch {
            layoutFragments.visibility = View.GONE
            (includeError as TextView).run {
                visibility = View.VISIBLE
                text = if (text.isNullOrBlank()) errorMessage else text
            }
        }
    }
}