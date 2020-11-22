package com.innocent.learn.autoreservation.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.innocent.learn.autoreservation.database.ReservationDao
import com.innocent.learn.autoreservation.database.ReservationDatabase
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.network.ReservationRemote
import java.util.concurrent.Executors

private const val DATABASE_NAME = "slot-database"

class ReservationRepository private constructor(context: Context) {
	private val reservationRemote = ReservationRemote(context)

	private val reservationDatabase: ReservationDatabase = Room.databaseBuilder(
		context.applicationContext,
		ReservationDatabase::class.java,
		DATABASE_NAME
	).build()
	private val reservationDao: ReservationDao = reservationDatabase.reservationDao()
	private val executor = Executors.newSingleThreadExecutor()

	fun fetchSlots(cookie: String): LiveData<List<Slot>> = reservationRemote.fetchSlots(cookie)

	fun subscribingToSlot(cookie: String, slotId: Int) = reservationRemote.subscribingToSlot(cookie, slotId)

	fun getSlotList(): LiveData<List<Slot>> = reservationDao.getSlotList()

	fun getSlot(id: Int): LiveData<Slot?> = reservationDao.getSlot(id)

	fun addSlot(slot: Slot) {
		executor.execute {
			reservationDao.addSlot(slot)
		}
	}

	fun deleteAllSlot() {
		executor.execute {
			reservationDao.deleteAllSlot()
		}
	}

	companion object {
		private var instance: ReservationRepository? = null

		fun initialize(context: Context) {
			if (instance == null) {
				instance = ReservationRepository(context)
			}
		}

		fun get(): ReservationRepository {
			return instance
				?: throw IllegalStateException("ReservationRepository must be initialized")
		}
	}
}