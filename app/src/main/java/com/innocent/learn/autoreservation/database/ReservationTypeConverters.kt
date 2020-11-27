package com.innocent.learn.autoreservation.database

import androidx.room.TypeConverter
import java.util.Date

class ReservationTypeConverters {

	@TypeConverter
	fun fromDate(date: Date?): Long? = date?.time

	@TypeConverter
	fun toDate(millisSinceEpoch: Long?): Date? = millisSinceEpoch?.let {
		return Date(it)
	}

}