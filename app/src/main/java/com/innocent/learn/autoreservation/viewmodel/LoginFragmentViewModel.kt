package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookieHelper

private const val RESERVATION_ID_MAX_LENGTH = 428

class LoginFragmentViewModel(private val app: Application) : AndroidViewModel(app) {
	private val _reservationRepository = ReservationRepository.get()

	fun fetchSlotList(reservationId: String): LiveData<List<Slot>> {
		return _reservationRepository.fetchSlotList(CookieHelper.getReservationCookie(reservationId))
	}

	fun addSlotList(slotList: List<Slot>) {
		_reservationRepository.addSlotList(slotList)
	}

	fun isValidReservationId(reservationId: String): Boolean {
		val reservationIdAfterTrim = reservationId.trim()
		if (reservationIdAfterTrim.length == RESERVATION_ID_MAX_LENGTH
			&& reservationIdAfterTrim.isNotEmpty()
			&& reservationIdAfterTrim.last() == '='
		) {
			return true
		}
		return false
	}

}
