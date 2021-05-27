package com.innocent.learn.autoreservation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.innocent.learn.autoreservation.model.Slot

@Dao
interface ReservationDao {
	
	@Query("SELECT * FROM slot")
	suspend fun getSlotList(): List<Slot>
	
	@Query("SELECT * FROM slot WHERE id = :id")
	suspend fun getSlot(id: Int): Slot
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addSlot(slot: Slot)
	
	@Query("DELETE FROM slot")
	suspend fun deleteAllSlot()
	
	@Update
	suspend fun updateSlot(slot: Slot)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun addSlotList(slotList: List<Slot>)
	
}