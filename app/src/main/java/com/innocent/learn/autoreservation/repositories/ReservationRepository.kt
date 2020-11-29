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

	// network

	fun fetchSlotList(cookie: String): LiveData<List<Slot>> = reservationRemote.fetchSlotList(cookie)

	fun subscribe(cookie: String, slotId: Int): LiveData<Slot> = reservationRemote.subscribe(cookie, slotId)

	fun unsubscribe(cookie: String, slotId: Int): LiveData<Slot> = reservationRemote.unsubscribe(cookie, slotId)

	// database

	fun getSlotList(): LiveData<List<Slot>> = reservationDao.getSlotList()

	fun getSlot(id: Int): LiveData<Slot?> = reservationDao.getSlot(id)

	fun addSlot(slot: Slot) {
		reservationDao.addSlot(slot)
	}

	fun deleteAllSlot() {
		executor.execute {
			reservationDao.deleteAllSlot()
		}
	}

	fun updateSlot(slot: Slot) {
		executor.execute {
			reservationDao.updateSlot(slot)
		}
	}

	fun addSlotList(slotList: List<Slot>) {
		executor.execute {
			reservationDao.addSlotList(slotList)
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