package com.innocent.learn.autoreservation.network.api.ftnetwork

import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "ReservationInterceptor"

class ReservationInterceptor() : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()

		// code for intercepting network requests

		return chain.proceed(chain.request())
	}
}