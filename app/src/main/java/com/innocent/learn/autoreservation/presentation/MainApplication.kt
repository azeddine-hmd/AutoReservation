package com.innocent.learn.autoreservation.presentation

import android.app.Application

@Suppress("unused")
class MainApplication : Application() {

	override fun onCreate() {
		super.onCreate()
		Singletons.initializeSingletons(this)
	}

}