package com.innocent.learn.autoreservation.network.api.ftnetwork

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "ReservationInterceptor"

class ReservationInterceptor() : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()

		val requestPath = request.url().encodedPath()
		Log.d(TAG, "intercept: path = $requestPath")

		val requestMethod = request.method()
		Log.d(TAG, "intercept: method = $requestMethod")

		return chain.proceed(chain.request())
	}
}