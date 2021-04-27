package com.innocent.learn.autoreservation.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.utils.CustomToast
import com.innocent.learn.autoreservation.viewmodel.ReservationViewModel
import com.innocent.learn.autoreservation.viewmodel.SubscribeViewModel

private const val TAG = "SubscribeDialog"
private const val INITIAL_COLOR = -16743049

enum class SubscribeButtonState {
	SUBSCRIBE, UNSUBSCRIBE
}

class SubscribeDialog : BottomSheetDialogFragment() {
	private lateinit var viewModel: SubscribeViewModel
	private lateinit var reservationViewModel: ReservationViewModel
	private val args: SubscribeDialogArgs by navArgs()
	private lateinit var subscribeButton: Button
	private lateinit var cancelButton: Button
	private lateinit var slot: Slot

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider(requireActivity()).get(SubscribeViewModel::class.java)
		reservationViewModel =
			ViewModelProvider(requireActivity()).get(ReservationViewModel::class.java)

		val foundSlot = reservationViewModel.slotList.find { args.slotId == it.id }
		if (foundSlot != null) {
			this.slot = foundSlot
		} else {
			Log.d(TAG, "onCreate: slot not found in list'")
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.dialog_subscribe, container, false)
		subscribeButton = view.findViewById(R.id.subscribe_button)
		cancelButton = view.findViewById(R.id.cancel_button)

		cancelButton.setOnClickListener {
			dismiss()
		}

		subscribeButton.setOnClickListener {
			if (slot.isSubscribed) {
				viewModel.unsubscribe(slot.id).observe(viewLifecycleOwner) { newSlot ->
					CustomToast.showSuccess(requireContext(), R.string.unsubscribe_toast)
					setSubscribeButtonView(SubscribeButtonState.SUBSCRIBE)
					updateSlot(newSlot)
				}
			} else {
				viewModel.subscribe(slot.id).observe(viewLifecycleOwner) { newSlot ->
					CustomToast.showSuccess(requireContext(), R.string.subscribe_toast)
					setSubscribeButtonView(SubscribeButtonState.UNSUBSCRIBE)
					updateSlot(newSlot)
				}
			}
		}

		updateSlotView(slot)

		return view
	}

	private fun updateSlotView(slot: Slot) {
		if (slot.isSubscribed) {
			setSubscribeButtonView(SubscribeButtonState.UNSUBSCRIBE)
		} else {
			setSubscribeButtonView(SubscribeButtonState.SUBSCRIBE)
		}
	}


	private fun updateSlot(newSlot: Slot) {
		val slotIndex = reservationViewModel.slotList.indexOf(slot)
		if (slotIndex != -1) {
			reservationViewModel.slotList[slotIndex] = newSlot
			reservationViewModel.updateViewPager(args.position)
			slot = newSlot
		} else {
			Log.d(TAG, "slot could not be found")
		}
	}

	private fun setSubscribeButtonView(state: SubscribeButtonState) {
		when (state) {
			SubscribeButtonState.SUBSCRIBE -> {
				subscribeButton.setText(R.string.subscribe_button_dialog)
				subscribeButton.setTextColor(INITIAL_COLOR)
			}
			SubscribeButtonState.UNSUBSCRIBE -> {
				subscribeButton.setText(R.string.unsubscribe_button_dialog)
				subscribeButton.setTextColor(Color.RED)
			}
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "onDestroy: executing...")

	}

}