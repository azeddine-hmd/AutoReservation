package com.innocent.learn.autoreservation.repositories

import com.innocent.learn.autoreservation.database.ReservationDao
import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.network.ReservationApi
import com.innocent.learn.autoreservation.network.model.SlotDtoMapper
import com.innocent.learn.autoreservation.network.response.ReservationResponse
import retrofit2.HttpException

private const val TAG = "ReservationRepository"

class ReservationRepository private constructor(
	private val network: ReservationApi,
	private val dao: ReservationDao
) {
	
	suspend fun fetchSlotList(): ReservationResponse<List<Slot>> = try {
		val response = network.fetchSlotList()
		val slotList = SlotDtoMapper.toDomainList(response)
		dao.addSlotList(slotList)
		ReservationResponse.Success(slotList)
	} catch (cause: HttpException) {
		val message: String = cause.message()
		ReservationResponse.Failure(Throwable(message, cause))
	} catch (cause: Exception) {
		ReservationResponse.Failure(Throwable("Unable to fetch slot list", cause))
	}
	
	suspend fun subscribe(slotId: Int): ReservationResponse<Slot> = try {
		val response = network.subscribe(slotId)
		val slot = SlotDtoMapper.mapToDomainModel(response)
		dao.updateSlot(slot)
		ReservationResponse.Success(slot)
	} catch (cause: HttpException) {
		val message: String = cause.message()
		ReservationResponse.Failure(Throwable(message, cause))
	} catch (cause: Throwable) {
		ReservationResponse.Failure(Throwable("Unable to subscribe", cause))
	}
	
	suspend fun unsubscribe(slotId: Int): ReservationResponse<Slot> = try {
		val response = network.unsubscribe(slotId)
		val slot = SlotDtoMapper.mapToDomainModel(response)
		dao.updateSlot(slot)
		ReservationResponse.Success(slot)
	} catch (cause: HttpException) {
		val message: String = cause.message()
		ReservationResponse.Failure(Throwable(message, cause))
	} catch (cause: Throwable) {
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