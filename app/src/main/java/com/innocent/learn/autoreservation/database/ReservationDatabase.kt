package com.innocent.learn.autoreservation.database

import androidx.room.*
import com.innocent.learn.autoreservation.model.Slot

@Database(entities = [Slot::class], version = 1)
@TypeConverters(ReservationTypeConverters::class)
abstract class ReservationDatabase : RoomDatabase() {

	abstract fun reservationDao(): ReservationDao

}