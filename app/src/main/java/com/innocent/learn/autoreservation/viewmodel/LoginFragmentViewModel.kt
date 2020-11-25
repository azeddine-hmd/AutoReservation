package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository

private const val TAG = "LoginFragmentViewModel"
private const val RESERVATION_ID_MAX_LENGTH = 428

class LoginFragmentViewModel(private val app: Application) : AndroidViewModel(app) {
	private val _reservationRepository = ReservationRepository.get()
	private val mutableReservationId = MutableLiveData<String>()

	val slotListLiveData: LiveData<List<Slot>> =
		Transformations.switchMap(mutableReservationId) { reservationId ->
			_reservationRepository.fetchSlots("reservation_system=$reservationId")
		}

	fun fetchSlots(reservationId: String) {
		mutableReservationId.value = reservationId
	}

	fun addSlot(slot: Slot) {
		_reservationRepository.addSlot(slot)
	}

	fun isValideReservationId(reservationId: String): Boolean {
		val reservationIdAfterTrim = reservationId.trim()
		Log.d(TAG, "validating reservation id")
		if (reservationIdAfterTrim.length == RESERVATION_ID_MAX_LENGTH
			&& reservationIdAfterTrim.isNotEmpty()
			&& reservationIdAfterTrim.last() == '='
		) {
			return true
		}
		return false
	}

	fun addSlotList(slotList: List<Slot>) {
		_reservationRepository.addSlotList(slotList)
	}
}
