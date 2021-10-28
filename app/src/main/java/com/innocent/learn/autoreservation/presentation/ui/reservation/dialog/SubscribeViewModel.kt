package com.innocent.learn.autoreservation.presentation.ui.reservation.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.network.response.ReservationResponse
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import kotlinx.coroutines.launch

class SubscribeViewModel() : ViewModel() {
	private val _reservationRepository = ReservationRepository.get()
	
	fun getSlotFromId(id: Int): LiveData<Slot> {
		val liveData = MutableLiveData<Slot>()
		viewModelScope.launch() {
			liveData.value = _reservationRepository.getSlot(id)
		}
		return liveData
	}
	
	fun subscribeButtonClicked(slot: Slot): LiveData<ReservationResponse<Slot>> {
		val liveData = MutableLiveData<ReservationResponse<Slot>>()
		viewModelScope.launch() {
			if (slot.isSubscribed) {
				liveData.value = _reservationRepository.unsubscribe(slot.id)
			} else {
				liveData.value = _reservationRepository.subscribe(slot.id)
			}
		}
		return liveData
	}
	
}