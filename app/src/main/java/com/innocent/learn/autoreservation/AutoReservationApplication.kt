package com.innocent.learn.autoreservation

import android.app.Application
import com.innocent.learn.autoreservation.repositories.ReservationRepository

@Suppress("unused")
class AutoReservationApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		ReservationRepository.initialize(this)
	}

}