package com.innocent.learn.autoreservation.network

import com.innocent.learn.autoreservation.model.Slot
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ReservationApi {

	@GET("/api/me/events")
	suspend fun fetchSlotList(): List<Slot>

	@POST("/api/me/events/{id}")
	suspend fun subscribe(slotId: Int): Slot

	@DELETE("/api/me/events/{id}")
	suspend fun unsubscribe(slotId: Int): Slot

}