package com.innocent.learn.autoreservation.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Slot(
	@PrimaryKey val id: Int,
	val begin: Date,
	val end: Date,
	val cluster: Int,
	val reservedPlaces: Int,
	var isSubscribed: Boolean,
	var isInBotList: Boolean,
)