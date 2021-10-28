package com.innocent.learn.autoreservation.presentation.ui.reservation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.databinding.DialogSubscribeBinding
import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.network.response.ReservationResponse
import com.innocent.learn.autoreservation.domain.util.CustomToast

private const val TAG = "SubscribeDialog"

class SubscribeDialog : BottomSheetDialogFragment() {
	private val viewModel: SubscribeViewModel by viewModels()
	private lateinit var binding: DialogSubscribeBinding
	private val args: SubscribeDialogArgs by navArgs()
	private lateinit var slot: Slot
	private var subscribeColor = 0
	private var unsubscribeColor = 0
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel.getSlotFromId(args.slotId).observe(this) { slot = it }
		subscribeColor = resources.getColor(R.color.subscribe, requireActivity().theme)
		unsubscribeColor = resources.getColor(R.color.unsubscribe, requireActivity().theme)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = DialogSubscribeBinding.inflate(layoutInflater)
		
		binding.cancelButton.setOnClickListener {
			dismiss()
		}
		
		binding.subscribeButton.setOnClickListener {
			viewModel.subscribeButtonClicked(slot).observe(viewLifecycleOwner) { response ->
				when (response) {
					is ReservationResponse.Success -> {
						slot = response.data
						updateSubscribeButtonView(slot.isSubscribed)
					}
					is ReservationResponse.Failure -> {
						response.error.message?.let {
							CustomToast.showError(requireContext(), it)
						}
					}
				}
			}
		}
		
		return binding.root
	}
	
	private fun updateSubscribeButtonView(isSubscribed: Boolean) {
		if (isSubscribed) {
			binding.subscribeButton.setText(R.string.subscribe_button_dialog)
			binding.subscribeButton.setTextColor(subscribeColor)
		} else {
			binding.subscribeButton.setText(R.string.unsubscribe_button_dialog)
			binding.subscribeButton.setTextColor(unsubscribeColor)
		}
	}
	
}