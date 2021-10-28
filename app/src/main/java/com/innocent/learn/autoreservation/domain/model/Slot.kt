package com.innocent.learn.autoreservation.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.innocent.learn.autoreservation.R
import java.util.Date

@Entity
data class Slot(
	@PrimaryKey val id: Int,
	val begin: Date,
	val end: Date,
	val cluster: Int,
	val reservedPlaces: Int,
	var isSubscribed: Boolean,
)