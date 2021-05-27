package com.innocent.learn.autoreservation.repositories

import android.util.Log
import com.google.gson.JsonParser
import com.innocent.learn.autoreservation.database.ReservationDao
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.network.ReservationApi
import com.innocent.learn.autoreservation.network.ReservationResponse
import retrofit2.HttpException

private const val TAG = "ReservationRepository"

class ReservationRepository private constructor(
	private val network: ReservationApi,
	private val dao: ReservationDao
) {
	
	suspend fun fetchSlotList(): ReservationResponse<List<Slot>> = try {
		val slotList = network.fetchSlotList()
		Log.i(TAG, "fetchSlotList: $slotList")
		dao.addSlotList(slotList)
		ReservationResponse.Success(slotList)
	} catch (cause: HttpException) {
		val errorJson = cause.response()?.errorBody()?.string()
		val message = JsonParser().parse(errorJson).asJsonObject["message"].asString
		Log.e(TAG, "fetchSlotList: ${message}")
		ReservationResponse.Failure(Throwable(message, cause))
	} catch (cause: Throwable) {
		Log.e(TAG, "fetchSlotList: ${cause.message}")
		ReservationResponse.Failure(Throwable("Unable to fetch slot list", cause))
	}
	
	suspend fun subscribe(slotId: Int): ReservationResponse<Slot> = try {
		val slot = network.subscribe(slotId)
		Log.i(TAG, "fetchSlotList: $slot")
		dao.updateSlot(slot)
		ReservationResponse.Success(slot)
	} catch (cause: Throwable) {
		Log.e(TAG, "fetchSlotList: ${cause.message}")
		ReservationResponse.Failure(Throwable("Unable to subscribe", cause))
	}
	
	suspend fun unsubscribe(slotId: Int): ReservationResponse<Slot> = try {
		val slot = network.unsubscribe(slotId)
		Log.i(TAG, "fetchSlotList: $slot")
		dao.updateSlot(slot)
		ReservationResponse.Success(slot)
	} catch (cause: Throwable) {
		Log.e(TAG, "fetchSlotList: ${cause.message}")
		ReservationResponse.Failure(Throwable("Unable to unsubscribe", cause))
	}
	
	suspend fun getSlotList(): List<Slot> = dao.getSlotList()
	
	suspend fun getSlot(id: Int): Slot = dao.getSlot(id)
	
	suspend fun addSlot(slot: Slot) = dao.addSlot(slot)
	
	suspend fun deleteAllSlots() = dao.deleteAllSlot()
	
	suspend fun updateSlot(slot: Slot) = dao.updateSlot(slot)
	
	suspend fun addSlotList(slotList: List<Slot>) = dao.addSlotList(slotList)
	
	companion object {
		private var instance: ReservationRepository? = null
		
		fun initialize(network: ReservationApi, dao: ReservationDao) {
			if (instance == null) {
				instance = ReservationRepository(network, dao)
			}
		}
		
		fun get(): ReservationRepository {
			return instance
				?: throw IllegalStateException("ReservationRepository must be initialized")
		}
	}
	
}