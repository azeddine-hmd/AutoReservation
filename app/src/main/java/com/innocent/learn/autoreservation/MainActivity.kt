package com.innocent.learn.autoreservation

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.innocent.learn.autoreservation.utils.CookiePreference

class MainActivity : AppCompatActivity() {
	private lateinit var navController: NavController
	private lateinit var toolbar: Toolbar
	private lateinit var bottomNavigation: BottomNavigationView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
				as NavHostFragment
		navController = navHostFragment.findNavController()
		toolbar = findViewById(R.id.toolbar)
		bottomNavigation = findViewById(R.id.bottom_navigation)
		handleNavigationGlobalComponentVisibility()
		setSupportActionBar(toolbar)
		val appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.login_fragment,
				R.id.reservation_fragment,
				R.id.bot_fragment,
			)
		)
		setupActionBarWithNavController(this, navController, appBarConfiguration)
		bottomNavigation.setupWithNavController(navController)

	}

	private fun handleNavigationGlobalComponentVisibility() {
		navController.addOnDestinationChangedListener { _, destination, _ ->
			when (destination.id) {
				R.id.login_fragment -> {
					bottomNavigation.visibility = View.GONE
					toolbar.visibility = View.GONE
				}
				else -> {
					bottomNavigation.visibility = View.VISIBLE
					toolbar.visibility = View.VISIBLE
				}
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.options_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			R.id.options_menu_reset -> {
				CookiePreference.setReservationId(this, "")
				navController.navigate(R.id.login_fragment)
				return true
			}
			else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
		}
	}

}