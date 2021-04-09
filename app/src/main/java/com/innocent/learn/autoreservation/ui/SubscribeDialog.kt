package com.innocent.learn.autoreservation.ui

import android.graphics.Color
import android.os.Bundle
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
import com.innocent.learn.autoreservation.viewmodel.ReservationFragmentViewModel
import com.innocent.learn.autoreservation.viewmodel.SubscribeDialogViewModel

private const val TAG = "SubscribeDialog"
private const val INITIAL_COLOR = -16743049

enum class SubscribeButtonState {
	SUBSCRIBE, UNSUBSCRIBE
}

class SubscribeDialog : BottomSheetDialogFragment() {
	private lateinit var viewModel: SubscribeDialogViewModel
	private lateinit var reservationViewModel: ReservationFragmentViewModel
	private val args: SubscribeDialogArgs by navArgs()
	private lateinit var subscribeButton: Button
	private lateinit var cancelButton: Button
	private lateinit var slot: Slot

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider(requireActivity()).get(SubscribeDialogViewModel::class.java)
		reservationViewModel =
			ViewModelProvider(requireActivity()).get(ReservationFragmentViewModel::class.java)

		viewModel.getSlot(args.slotId)
		viewModel.slotLiveData.observe(this) { slot ->
			if (slot != null) {
				this.slot = slot
				if (slot.isSubscribed) {
					setSubscribeButtonView(SubscribeButtonState.UNSUBSCRIBE)
				} else {
					setSubscribeButtonView(SubscribeButtonState.SUBSCRIBE)
				}
			}
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
				val unsubscribeLiveData = viewModel.unsubscribe(slot.id)
				unsubscribeLiveData.observe(viewLifecycleOwner) { newSlot ->
					CustomToast.showSuccess(requireContext(), R.string.unsubscribe_toast)
					setSubscribeButtonView(SubscribeButtonState.SUBSCRIBE)

					val slotIndex = reservationViewModel.slotList.indexOf(slot)
					if (slotIndex != -1) {
						reservationViewModel.slotList[slotIndex] = newSlot
						reservationViewModel.updateViewPager(args.position)
					}
					dismiss()
				}
			} else {
				val subscribeLiveData = viewModel.subscribe(slot.id)
				subscribeLiveData.observe(viewLifecycleOwner) { newSlot ->
					CustomToast.showSuccess(requireContext(), R.string.subscribe_toast)
					setSubscribeButtonView(SubscribeButtonState.UNSUBSCRIBE)

					val slotIndex = reservationViewModel.slotList.indexOf(slot)
					if (slotIndex != -1) {
						reservationViewModel.slotList[slotIndex] = newSlot
						reservationViewModel.updateViewPager(args.position)
					}
					dismiss()
				}
			}
		}



		return view
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

}