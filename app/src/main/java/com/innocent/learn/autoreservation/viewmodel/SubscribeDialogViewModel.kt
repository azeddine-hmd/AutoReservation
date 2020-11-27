package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookiePreference

class SubscribeDialogViewModel(private val app: Application) : AndroidViewModel(app) {
	private val _reservationRepository = ReservationRepository.get()
	lateinit var getSlotLiveData: LiveData<Slot?>

	fun subscribe(slotId: Int) {
		val reservationId = CookiePreference.getStoredReservationId(app)
		_reservationRepository.subscribe("reservation_system=$reservationId", slotId)
	}

	fun unsubscribe(slotId: Int) {
		val reservationId = CookiePreference.getStoredReservationId(app)
		_reservationRepository.unsubscribe("reservation_system=$reservationId", slotId)
	}

	fun getSlot(slotId: Int) {
		getSlotLiveData = _reservationRepository.getSlot(slotId)
	}

	fun updateSlot(slot: Slot) {
		_reservationRepository.updateSlot(slot)
	}

}