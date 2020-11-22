package com.innocent.learn.autoreservation.network

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.network.api.ftnetwork.ReservationApi
import com.innocent.learn.autoreservation.network.api.ftnetwork.ReservationDeserializer
import com.innocent.learn.autoreservation.network.api.ftnetwork.ReservationInterceptor
import com.innocent.learn.autoreservation.network.api.ftnetwork.Slots
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://reservation.42network.org/"

class ReservationRemote(private val context: Context) {
	private val reservationApi: ReservationApi

	init {
		val gson = GsonBuilder()
			.registerTypeAdapter(Slots::class.java, ReservationDeserializer())
			.create()
		val client = OkHttpClient.Builder()
			.addInterceptor(ReservationInterceptor())
			.build()
		val gsonConverterFactory = GsonConverterFactory.create(gson)
		val retrofit = Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(gsonConverterFactory)
			.client(client)
			.build()
		reservationApi = retrofit.create(ReservationApi::class.java)
	}

	fun fetchSlots(cookie: String): LiveData<List<Slot>> {
		val responseLiveData = MutableLiveData<List<Slot>>()
		val reservationRequest: Call<Slots> = reservationApi.fetchReservation(cookie)
		reservationRequest.enqueue(object : Callback<Slots> {
			override fun onResponse(call: Call<Slots>, response: Response<Slots>) {
				if (!response.isSuccessful) {
					responseFailed()
				} else {
					val slots: Slots? = response.body()
					val slotsList: List<Slot> = slots?.slots ?: emptyList()
					responseLiveData.value = slotsList
				}
			}

			override fun onFailure(call: Call<Slots>, t: Throwable) {
				networkIsNotAvailable()
			}
		})
		return responseLiveData
	}

	fun subscribingToSlot(cookie: String, slotId: Int) {
		val subscribingRequest = reservationApi.subscribingToSlot(cookie, slotId)
		subscribingRequest.enqueue(object : Callback<Unit> {
			override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
				if (!response.isSuccessful) {
					responseFailed()
				} else {
					//TODO: modify subscribing request return type to string
				}
			}
			override fun onFailure(call: Call<Unit>, t: Throwable) {
				networkIsNotAvailable()
			}

		})
	}

	private fun networkIsNotAvailable() {
		Toast.makeText(context, R.string.network_fail_connection, Toast.LENGTH_LONG)
			.show()
	}

	private fun responseFailed() {
		Toast.makeText(context, R.string.network_other_errors, Toast.LENGTH_LONG)
			.show()
	}
}