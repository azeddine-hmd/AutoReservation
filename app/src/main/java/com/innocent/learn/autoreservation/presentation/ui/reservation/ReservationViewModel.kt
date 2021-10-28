package com.innocent.learn.autoreservation.presentation.ui.reservation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.domain.model.Page
import com.innocent.learn.autoreservation.network.response.ReservationResponse
import com.innocent.learn.autoreservation.repositories.ReservationRepository
import kotlinx.coroutines.launch

private const val TAG = "ReservationViewModel"

class ReservationViewModel() : ViewModel() {
	private val _reservationRepository = ReservationRepository.get()
	
	fun getSlotList(): LiveData<List<Slot>> {
		val liveData = MutableLiveData<List<Slot>>()
		viewModelScope.launch {
			liveData.value = _reservationRepository.getSlotList()
		}
		return liveData
	}
	
	fun swipeRefreshTriggered(): LiveData<ReservationResponse<List<Slot>>> {
		val livedata = MutableLiveData<ReservationResponse<List<Slot>>>()
		viewModelScope.launch {
			livedata.value = _reservationRepository.fetchSlotList()
		}
		return livedata
	}
	
	fun pageListConverter(slotList: List<Slot>): List<Page> {
		val mutableSlotList = slotList.toMutableList()
		val slotPageList = mutableListOf<Page>()
		while (mutableSlotList.isNotEmpty()) {
			try {
				val topLeft = mutableSlotList.removeFirst()
				val topRight = mutableSlotList.removeFirst()
				val bottomLeft = mutableSlotList.removeFirst()
				val bottomRight = mutableSlotList.removeFirst()
				val slotPage = Page(topLeft, topRight, bottomLeft, bottomRight)
				slotPageList.add(slotPage)
			} catch (e: NoSuchElementException) {
				throw IllegalStateException("slotPageConverter: ${e.cause}")
			}
		}
		return slotPageList
	}
	
}