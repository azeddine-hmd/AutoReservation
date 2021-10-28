package com.innocent.learn.autoreservation.network.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class SlotDto(
	@SerializedName("Event")
	val event: Event,
	
	@SerializedName("is_subscribed")
	val isSubscribed: Boolean,
) {
	data class Event(
		@SerializedName("event_id")
		val eventId: Int,
		
		@SerializedName("begin_at")
		val beginAt: Date,
		
		@SerializedName("end_at")
		val endAt: Date,
		
		@SerializedName("zone_id")
		val zoneId: Int,
		
		@SerializedName("number_of_places_reserved")
		val numberOfPlacesReserved: Int,
	)
}
