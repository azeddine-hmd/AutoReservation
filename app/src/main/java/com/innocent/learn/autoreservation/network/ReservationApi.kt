package com.innocent.learn.autoreservation.network

import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.network.model.SlotDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ReservationApi {
	
	@GET("/api/me/events")
	suspend fun fetchSlotList(): List<SlotDto>
	
	@POST("/api/me/events/{id}")
	suspend fun subscribe(slotId: Int): SlotDto
	
	@DELETE("/api/me/events/{id}")
	suspend fun unsubscribe(slotId: Int): SlotDto
	
}