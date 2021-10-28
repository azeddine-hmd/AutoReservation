package com.innocent.learn.autoreservation.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import com.innocent.learn.autoreservation.domain.util.CookieHelper
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