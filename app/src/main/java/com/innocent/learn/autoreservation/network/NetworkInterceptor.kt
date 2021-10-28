package com.innocent.learn.autoreservation.network

import com.innocent.learn.autoreservation.domain.util.CookieHelper
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "ReservationInterceptor"

class NetworkInterceptor() : Interceptor {
	
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		
		val newRequest = request.newBuilder()
			.addHeader("Cookie", "reservation_system=" + CookieHelper.getReservationId())
			.build()
		
		return chain.proceed(newRequest)
	}
	
}