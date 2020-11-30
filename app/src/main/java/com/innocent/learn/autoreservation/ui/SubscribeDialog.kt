package com.innocent.learn.autoreservation.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.utils.CustomToast
import com.innocent.learn.autoreservation.viewmodel.SubscribeDialogViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "SubscribeDialog"
private const val INITIAL_COLOR = -16743049

enum class SubscribeButtonState {
	SUBSCRIBE, UNSUBSCRIBE
}

enum class InBotListButtonState {
	IN, OUT
}

class SubscribeDialog : BottomSheetDialogFragment() {
	private lateinit var viewModel: SubscribeDialogViewModel
	private val args: SubscribeDialogArgs by navArgs()
	private lateinit var subscribeButton: Button
	private lateinit var cancelButton: Button
	private lateinit var slot: Slot
	private lateinit var dateTextView: TextView
	private lateinit var timeTextView: TextView
	private lateinit var clusterTextView: TextView
	private lateinit var reservedPlacesTextView: TextView
	private lateinit var addBotToListButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel = ViewModelProvider(this).get(SubscribeDialogViewModel::class.java)

		viewModel.getSlot(args.slotId)
		viewModel.slotLiveData.observe(this) { slot ->
			if (slot != null) {
				this.slot = slot
				bindSlotTextViewData()
				if (slot.isSubscribed) {
					setSubscribeButtonView(SubscribeButtonState.UNSUBSCRIBE)
				} else {
					setSubscribeButtonView(SubscribeButtonState.SUBSCRIBE)
				}
				if (slot.isInBotList) {
					setInBotListButton(InBotListButtonState.OUT)
				} else {
					setInBotListButton(InBotListButtonState.IN)
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
		dateTextView = view.findViewById(R.id.date_text_view)
		timeTextView = view.findViewById(R.id.time_text_view)
		clusterTextView = view.findViewById(R.id.cluster_text_view)
		reservedPlacesTextView = view.findViewById(R.id.reserved_places_text_view)
		addBotToListButton = view.findViewById(R.id.add_to_bot_list_button)

		addBotToListButton.setOnClickListener {
			if (slot.isInBotList) {
				slot.isInBotList = false
				viewModel.updateSlot(slot)
			} else {
				slot.isInBotList = true
				viewModel.updateSlot(slot)
			}
		}

		cancelButton.setOnClickListener {
			dismiss()
		}

		subscribeButton.setOnClickListener {
			if (slot.isSubscribed) {
				val unsubscribeLiveData = viewModel.unsubscribe(slot.id)
				unsubscribeLiveData.observe(viewLifecycleOwner) { newSlot ->
					CustomToast.showSuccess(requireContext(), R.string.unsubscribe_toast)
					setSubscribeButtonView(SubscribeButtonState.SUBSCRIBE)

					Log.i(TAG, "slot state before subscribing: ${this.slot}")
					// updating slot accordingly
					if (this.slot.isInBotList) {
						newSlot.isInBotList = true
					}
					Log.i(TAG, "slot state after subscribing: $newSlot")

					viewModel.updateSlot(newSlot)
				}
			} else {
				val subscribeLiveData = viewModel.subscribe(slot.id)
				subscribeLiveData.observe(viewLifecycleOwner) { newSlot ->
					CustomToast.showSuccess(requireContext(), R.string.subscribe_toast)
					setSubscribeButtonView(SubscribeButtonState.UNSUBSCRIBE)

					Log.i(TAG, "slot state before subscribing: ${this.slot}")
					// updating slot accordingly
					if (this.slot.isInBotList) {
						newSlot.isInBotList = true
					}
					Log.i(TAG, "slot state after subscribing: ${this.slot}")

					viewModel.updateSlot(newSlot)
				}
			}
		}

		return view
	}

	private fun bindSlotTextViewData() {
		val date: String = SimpleDateFormat("EEE dd-MM-yyyy", Locale.getDefault()).format(slot.begin)
		val timeBegin: String = SimpleDateFormat("HHa", Locale.getDefault()).format(slot.begin)
		val timeEnd: String = SimpleDateFormat("HHa", Locale.getDefault()).format(slot.end)

		dateTextView.text = getString(R.string.list_item_date, date)
		timeTextView.text = getString(R.string.list_item_time, timeBegin, timeEnd)
		clusterTextView.text = getString(R.string.list_item_cluster, slot.cluster)
		reservedPlacesTextView.text = getString(R.string.list_item_reserved, slot.reservedPlaces)
	}

	private fun setInBotListButton(state: InBotListButtonState) {
		when (state) {
			InBotListButtonState.IN -> {
				addBotToListButton.setText(R.string.subscribe_dialog_button_add_bot_list)
				addBotToListButton.setTextColor(INITIAL_COLOR)
			}
			InBotListButtonState.OUT -> {
				addBotToListButton.setText(R.string.subscribe_dialog_button_remove_bot_list)
				addBotToListButton.setTextColor(Color.RED)
			}
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

}