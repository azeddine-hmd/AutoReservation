package com.innocent.learn.autoreservation.presentation

import android.content.Context
import androidx.room.Room
import com.innocent.learn.autoreservation.database.ReservationDao
import com.innocent.learn.autoreservation.database.ReservationDatabase
import com.innocent.learn.autoreservation.network.reservationNetwork
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.domain.util.CookieHelper

private val DATABASE_NAME = "slot-database"

object Singletons {
	
	fun initializeSingletons(context: Context) {
		// initializing repository
		val reservationDatabase by lazy {
			Room.databaseBuilder(
				context.applicationContext,
				ReservationDatabase::class.java,
				DATABASE_NAME
			).build()
		}
		val reservationDao: ReservationDao = reservationDatabase.reservationDao()
		ReservationRepository.initialize(reservationNetwork, reservationDao)
		
		// initializing Utils
		CookieHelper.initialize(context)
	
	}
	
}