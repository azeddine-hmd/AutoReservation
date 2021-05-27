package com.innocent.learn.autoreservation

import android.app.Application
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookieHelper
import dagger.hilt.android.HiltAndroidApp

@Suppress("unused")
class MainApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		Singletons.initializeSingletons(this)
	}

}