package com.innocent.learn.autoreservation.network.response

sealed class ReservationResponse<out T> {
	data class Success<T>(val data: T): ReservationResponse<T>()
	data class Failure<Nothing>(val error: Throwable): ReservationResponse<Nothing>()
}