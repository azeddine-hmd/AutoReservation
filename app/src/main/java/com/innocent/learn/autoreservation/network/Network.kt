package com.innocent.learn.autoreservation.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.innocent.learn.autoreservation.model.Slot
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val BASE_URL = "https://reservation.42network.org/"

val reservationNetwork: ReservationApi by lazy {
	val gson: Gson = GsonBuilder()
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
	
	retrofit.create(ReservationApi::class.java)
}

//	fun fetchSlotList(cookie: String): List<Slot> {
//		try {
//			val slotList: List<Slot> = reservationApi.fetchReservation(cookie)
//		} catch (ex: Exception) {
//			Log.e(TAG, "fetchSlotList: exception triggered", )
//		}
//
//		reservationRequest.enqueue(object : Callback<List<Slot>> {
//			override fun onResponse(call: Call<List<Slot>>, response: Response<List<Slot>>) {
//				if (response.isSuccessful) {
//					val slotList: List<Slot>? = response.body()
//					val slotsList: List<Slot> = slotList ?: emptyList()
//					responseLiveData.value = slotsList
//				} else {
//					CustomToast.showError(context, R.string.network_other_errors)
//				}
//			}
//
//			override fun onFailure(call: Call<List<Slot>>, t: Throwable) {
//				responseLiveData.value = emptyList()
//				CustomToast.showError(context, R.string.network_fail_connection)
//			}
//		})
//
//	}
//
//	fun subscribe(cookie: String, slotId: Int): LiveData<Slot> {
//		val responseLiveData = MutableLiveData<Slot>()
//		val subscribingRequest = reservationApi.subscribe(cookie, slotId)
//
//		subscribingRequest.enqueue(object : Callback<Slot> {
//			override fun onResponse(call: Call<Slot>, response: Response<Slot>) {
//				if (response.isSuccessful) {
//					Log.i(TAG, "success subscribe json: ${response.body()}")
//					val slot: Slot? = response.body()
//					responseLiveData.value = slot
//				} else {
//					val error = deserializeJsonError(response.errorBody()?.string())
//					CustomToast.showError(context, error)
//				}
//			}
//
//			override fun onFailure(call: Call<Slot>, t: Throwable) {
//				CustomToast.showError(context, R.string.network_fail_connection)
//			}
//
//		})
//
//		return responseLiveData
//	}
//
//	fun unsubscribe(cookie: String, slotId: Int): LiveData<Slot> {
//		val responseLiveData = MutableLiveData<Slot>()
//		val subscribingRequest = reservationApi.unsubscribe(cookie, slotId)
//
//		subscribingRequest.enqueue(object : Callback<Slot> {
//			override fun onResponse(call: Call<Slot>, response: Response<Slot>) {
//				if (response.isSuccessful) {
//					Log.i(TAG, "success subscribe json: ${response.body()}")
//					val slot: Slot? = response.body()
//					responseLiveData.value = slot
//				} else {
//					val error = deserializeJsonError(response.errorBody()?.string())
//					CustomToast.showError(context, error)
//				}
//			}
//
//			override fun onFailure(call: Call<Slot>, t: Throwable) {
//				CustomToast.showError(context, R.string.network_fail_connection)
//			}
//
//		})
//
//		return responseLiveData
//	}
//
//}
//
//class ReservationError(message: String, cause: Throwable?): Throwable(message, cause)