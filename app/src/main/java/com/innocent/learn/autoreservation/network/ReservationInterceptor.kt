package com.innocent.learn.autoreservation.network

import com.innocent.learn.autoreservation.utils.CookieHelper
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "ReservationInterceptor"

class ReservationInterceptor() : Interceptor {
	
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.call().request()
		val newRequest = request.newBuilder()
			.addHeader("reservation_system", CookieHelper.getReservationId())
			.build()
		
		return chain.proceed(newRequest)
	}
	
}