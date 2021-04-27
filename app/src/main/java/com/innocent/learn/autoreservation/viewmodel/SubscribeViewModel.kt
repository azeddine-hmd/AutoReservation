package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookieHelper

class SubscribeViewModel(private val app: Application) : AndroidViewModel(app) {
	private val _reservationRepository = ReservationRepository.get()
	private val _slotIdLiveData = MutableLiveData<Int>()
	val slotLiveData: LiveData<Slot?> = Transformations.switchMap(_slotIdLiveData) { slotId ->
		_reservationRepository.getSlot(slotId)
	}

	fun subscribe(slotId: Int): LiveData<Slot> {
		return _reservationRepository.subscribe(CookieHelper.getReservationCookie(app), slotId)
	}

	fun unsubscribe(slotId: Int): LiveData<Slot> {
		return _reservationRepository.unsubscribe(CookieHelper.getReservationCookie(app), slotId)
	}

	fun getSlot(slotId: Int) {
		_slotIdLiveData.value = slotId
	}

	fun updateSlot(slot: Slot) {
		_reservationRepository.updateSlot(slot)
	}

}