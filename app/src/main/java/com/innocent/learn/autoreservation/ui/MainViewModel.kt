package com.innocent.learn.autoreservation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.utils.CookieHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
	private val _repository = ReservationRepository.get()
	
	fun resetReservationId() {
		CookieHelper.resetReservationId()
		viewModelScope.launch(Dispatchers.IO) {
			_repository.deleteAllSlots()
		}
	}
	
}