package com.innocent.learn.autoreservation.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.network.ReservationResponse
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
	private val _reservationRepository = ReservationRepository.get()
	
	fun loginButtonClicked(): LiveData<ReservationResponse<List<Slot>>> {
		val livedata = MutableLiveData<ReservationResponse<List<Slot>>>()
		viewModelScope.launch() {
			livedata.value = _reservationRepository.fetchSlotList()
		}
		return livedata
	}
	
}