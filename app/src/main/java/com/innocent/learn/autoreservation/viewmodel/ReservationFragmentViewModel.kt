package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookieHelper
import java.util.Date

class ReservationFragmentViewModel(private val app: Application) : AndroidViewModel(app) {

	private val _reservationRepository = ReservationRepository.get()
	val getSlotList: LiveData<List<Slot>> = _reservationRepository.getSlotList()
	val fetchSlotList: LiveData<List<Slot>>
		get() = _reservationRepository.fetchSlotList(CookieHelper.getReservationCookie(app))
	var slotList: List<Slot> = emptyList()

	fun addSlotList(slotList: List<Slot>) {
		_reservationRepository.deleteAllSlot()
		_reservationRepository.addSlotList(slotList)
	}

	fun updateSlotList(oldSlotList: List<Slot>, newSlotList: List<Slot>): List<Slot> {
		val slotList = mutableListOf<Slot>()
		for (newSlot in newSlotList) {
			for (oldSlot in oldSlotList) {
				if (newSlot.id == oldSlot.id && oldSlot.isInBotList) {
					newSlot.isInBotList = true
					slotList.add(newSlot)
				} else {
					slotList.add(newSlot)
				}
			}
		}

		return slotList.toList()
	}

	fun filterSlotListDependsOnTime(oldSlotList: List<Slot>): List<Slot> {
		val newSlotList = mutableListOf<Slot>()
		val currentDate = Date()
		for (oldSlot in oldSlotList) {
			if (!oldSlot.begin.before(currentDate)) {
				newSlotList.add(oldSlot)
			}
		}

		return newSlotList
	}

}