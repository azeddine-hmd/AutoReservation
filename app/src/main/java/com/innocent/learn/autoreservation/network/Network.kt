package com.innocent.learn.autoreservation.network

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://reservation.42network.org"

val reservationNetwork: ReservationApi by lazy {
	val client = OkHttpClient.Builder()
		.addInterceptor(NetworkInterceptor())
		.build()
	
	val retrofit = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.addConverterFactory(GsonConverterFactory.create(Gson()))
		.client(client)
		.build()
	
	retrofit.create(ReservationApi::class.java)
}
