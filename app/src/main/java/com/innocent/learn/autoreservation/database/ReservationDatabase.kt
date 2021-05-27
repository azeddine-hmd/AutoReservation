package com.innocent.learn.autoreservation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.innocent.learn.autoreservation.model.Slot

@Database(entities = [Slot::class], version = 1, exportSchema = false)
@TypeConverters(ReservationTypeConverters::class)
abstract class ReservationDatabase : RoomDatabase() {
	abstract fun reservationDao(): ReservationDao
}