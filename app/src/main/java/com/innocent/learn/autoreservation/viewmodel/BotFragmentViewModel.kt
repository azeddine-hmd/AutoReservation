package com.innocent.learn.autoreservation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.repositories.ReservationRepository

class BotFragmentViewModel(private val app: Application) : AndroidViewModel(app) {
	private val _reservationRepository = ReservationRepository.get()
	val botListLiveData: LiveData<List<Slot>>
		get() = _reservationRepository.getBotList()
	var botList: List<Slot> = emptyList()
}
