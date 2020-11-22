package com.innocent.learn.autoreservation.network.api.ftnetwork

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApi {

	@GET("/api/me/events")
	fun fetchReservation(@Header("Cookie") reservationId: String): Call<Slots>

	@POST("/api/me/events/{id}")
	fun subscribingToSlot(@Header("Cookie") reservationId: String, @Path("id") slotId: Int): Call<Unit>
}