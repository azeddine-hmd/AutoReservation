package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookiePreference

class ReservationFragmentViewModel(private val app: Application) : AndroidViewModel(app) {
	private val _reservationRepository = ReservationRepository.get()
	private val _mutableReservationId = MutableLiveData<String>()

	val slotsLiveData: LiveData<List<Slot>> =
		Transformations.switchMap(_mutableReservationId) { reservationId ->
			_reservationRepository.fetchSlots("reservation_system=$reservationId")
		}

	fun fetchSlots() {
		val reservationId = CookiePreference.getStoredReservationId(app)
		_mutableReservationId.value = reservationId
	}

	fun addSlotList(slotList: List<Slot>?) {
		_reservationRepository.deleteAllSlot()
		if (slotList != null) {
			for (slot in slotList) {
				_reservationRepository.addSlot(slot)
			}
		}
	}
}