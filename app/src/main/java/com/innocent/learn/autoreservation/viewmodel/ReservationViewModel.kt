package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.innocent.learn.autoreservation.model.PageSlot
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookieHelper
import java.util.Date

private const val TAG = "ReservationViewModel"

class ReservationViewModel(private val app: Application) : AndroidViewModel(app) {

	private val _reservationRepository = ReservationRepository.get()

	val getSlotList: LiveData<List<Slot>> = _reservationRepository.getSlotList()
	val fetchSlotList: LiveData<List<Slot>>
		get() = _reservationRepository.fetchSlotList(CookieHelper.getReservationCookie(app))

	private val _updateViewPager = MutableLiveData<Int>()
	val updateViewPager: LiveData<Int>
		get() = _updateViewPager

	var slotList: MutableList<Slot> = mutableListOf()
	var position = 0

	fun addSlotList(slotList: List<Slot>) {
		_reservationRepository.deleteAllSlot()
		_reservationRepository.addSlotList(slotList)
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

	fun convertSlotList(slotList: List<Slot>): List<PageSlot> {
		val pageSlotList: MutableList<PageSlot> = mutableListOf()
		val slotListMutable = slotList.toMutableList()
		while (slotListMutable.size != 0 && slotListMutable.size % 4 == 0) {
			val topLeft = slotListMutable.removeFirst()
			val topRight = slotListMutable.removeFirst()
			val bottomLeft = slotListMutable.removeFirst()
			val bottomRight = slotListMutable.removeFirst()
			pageSlotList.add(PageSlot(topLeft, topRight, bottomLeft, bottomRight))
		}

		return pageSlotList
	}

	fun updateViewPager(position: Int) {
		_updateViewPager.value = position
	}

}