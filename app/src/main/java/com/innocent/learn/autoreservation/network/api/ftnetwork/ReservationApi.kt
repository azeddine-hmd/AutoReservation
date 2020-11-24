package com.innocent.learn.autoreservation.network.api.ftnetwork

import retrofit2.Call
import retrofit2.http.*

interface ReservationApi {

	@GET("/api/me/events")
	fun fetchReservation(@Header("Cookie") reservationId: String): Call<Slots>

	@POST("/api/me/events/{id}")
	fun subscribe(@Header("Cookie") reservationId: String, @Path("id") slotId: Int): Call<Unit>

	@DELETE("/api/me/events/{id}")
	fun unsubscribe(@Header("Cookie") reservationId: String, @Path("id") slotId: Int): Call<Unit>
}