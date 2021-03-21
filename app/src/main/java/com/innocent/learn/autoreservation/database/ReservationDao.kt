package com.innocent.learn.autoreservation.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.innocent.learn.autoreservation.model.Slot

@Dao
interface ReservationDao {

	@Query("SELECT * FROM slot")
	fun getSlotList(): LiveData<List<Slot>>

	@Query("SELECT * FROM slot WHERE isInBotList = 1")
	fun getBotList(): LiveData<List<Slot>>

	@Query("SELECT * FROM slot WHERE id = :id")
	fun getSlot(id: Int): LiveData<Slot?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun addSlot(slot: Slot)

	@Query("DELETE FROM slot")
	fun deleteAllSlot()

	@Update
	fun updateSlot(slot: Slot)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun addSlotList(slotList: List<Slot>)

}