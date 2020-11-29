package com.innocent.learn.autoreservation.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.network.api.ftnetwork.*
import com.innocent.learn.autoreservation.network.api.ftnetwork.SlotsDeserializer.Companion.deserializeJsonError
import com.innocent.learn.autoreservation.utils.CustomToast
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "ReservationRemote"
private const val BASE_URL = "https://reservation.42network.org/"

class ReservationRemote(private val context: Context) {
	private val reservationApi: ReservationApi

	init {
		val gson: Gson = GsonBuilder()
			.registerTypeAdapter(Slots::class.java, SlotsDeserializer())
			.registerTypeAdapter(Slot::class.java, SlotDeserializer())
			.create()

		val client = OkHttpClient.Builder()
			.addInterceptor(ReservationInterceptor())
			.build()

		val gsonConverterFactory = GsonConverterFactory.create(gson)

		val retrofit = Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(ScalarsConverterFactory.create())
			.addConverterFactory(gsonConverterFactory)
			.client(client)
			.build()

		reservationApi = retrofit.create(ReservationApi::class.java)
	}

	fun fetchSlotList(cookie: String): LiveData<List<Slot>> {
		val responseLiveData = MutableLiveData<List<Slot>>()
		val reservationRequest: Call<Slots> = reservationApi.fetchReservation(cookie)

		reservationRequest.enqueue(object : Callback<Slots> {
			override fun onResponse(call: Call<Slots>, response: Response<Slots>) {
				if (response.isSuccessful) {
					val slots: Slots? = response.body()
					val slotsList: List<Slot> = slots?.slots ?: emptyList()
					responseLiveData.value = slotsList
				} else {
					CustomToast.showError(context, R.string.network_other_errors)
				}
			}

			override fun onFailure(call: Call<Slots>, t: Throwable) {
				CustomToast.showError(context, R.string.network_fail_connection)
			}
		})

		return responseLiveData
	}

	fun subscribe(cookie: String, slotId: Int): LiveData<Slot> {
		val responseLiveData = MutableLiveData<Slot>()
		val subscribingRequest = reservationApi.subscribe(cookie, slotId)

		subscribingRequest.enqueue(object : Callback<Slot> {
			override fun onResponse(call: Call<Slot>, response: Response<Slot>) {
				if (response.isSuccessful) {
					Log.i(TAG, "success subscribe json: ${response.body()}")
					val slot: Slot? = response.body()
					responseLiveData.value = slot
				} else {
					val error = deserializeJsonError(response.errorBody()?.string())
					CustomToast.showError(context, error)
				}
			}

			override fun onFailure(call: Call<Slot>, t: Throwable) {
				CustomToast.showError(context, R.string.network_fail_connection)
				Log.e(TAG, "onFailure: stack trace -> ${t.printStackTrace()}\n message -> ${t.message}", )
			}

		})

		return responseLiveData
	}

	fun unsubscribe(cookie: String, slotId: Int): LiveData<Slot> {
		val responseLiveData = MutableLiveData<Slot>()
		val subscribingRequest = reservationApi.unsubscribe(cookie, slotId)

		subscribingRequest.enqueue(object : Callback<Slot> {
			override fun onResponse(call: Call<Slot>, response: Response<Slot>) {
				if (response.isSuccessful) {
					Log.i(TAG, "success subscribe json: ${response.body()}")
					val slot: Slot? = response.body()
					responseLiveData.value = slot
				} else {
					val error = deserializeJsonError(response.errorBody()?.string())
					CustomToast.showError(context, error)
				}
			}

			override fun onFailure(call: Call<Slot>, t: Throwable) {
				CustomToast.showError(context, R.string.network_fail_connection)
				Log.e(TAG, "onFailure: stack trace -> ${t.stackTrace}\n message -> ${t.message}", )
			}

		})

		return responseLiveData
	}

}