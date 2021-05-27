package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private val viewModel: MainViewModel by viewModels()
	private lateinit var binding: ActivityMainBinding
	private lateinit var navController: NavController
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
		val navHostFragment =
			supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
		navController = navHostFragment.navController
		setSupportActionBar(binding.toolbar)
		val appBarConfiguration = AppBarConfiguration(
			setOf(
				R.id.login_fragment,
				R.id.reservation_fragment
			)
		)
		setupActionBarWithNavController(this, navController, appBarConfiguration)
		binding.bottomNavigation.setupWithNavController(navController)
		navController.addOnDestinationChangedListener { _, destination, _ ->
			when (destination.id) {
				R.id.login_fragment -> {
					binding.bottomNavigation.visibility = View.GONE
					binding.toolbar.visibility = View.GONE
				}
				else -> {
					binding.bottomNavigation.visibility = View.GONE
					binding.toolbar.visibility = View.VISIBLE
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
				viewModel.resetReservationId()
				navController.navigate(R.id.login_fragment)
				return true
			}
			else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
		}
	}
	
}