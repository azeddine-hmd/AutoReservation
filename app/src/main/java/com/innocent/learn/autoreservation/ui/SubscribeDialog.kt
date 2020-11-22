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
import com.innocent.learn.autoreservation.viewmodel.SubscribeDialogViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "SubscribeDialog"

class SubscribeDialog : BottomSheetDialogFragment() {
	private lateinit var subscribeDialogViewModel: SubscribeDialogViewModel
	private lateinit var subscribeButton: Button
	private lateinit var cancelButton: Button
	private val args: SubscribeDialogArgs by navArgs()
	private lateinit var slot: Slot
	private lateinit var dateTextView: TextView
	private lateinit var timeTextView: TextView
	private lateinit var clusterTextView: TextView
	private lateinit var reservedPlacesTextView: TextView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		subscribeDialogViewModel = ViewModelProvider(this).get(SubscribeDialogViewModel::class.java)
		subscribeDialogViewModel.getSlot(args.slotId)
		subscribeDialogViewModel.getSlotLiveData.observe(this, { slot ->
			if (slot != null) {
				this.slot = slot
				setSlotTextView()
				if (slot.isSubscribed) {
					subscribeButton.setText(R.string.unsubscribe_button_dialog)
					subscribeButton.setTextColor(Color.RED)
				} else {
					subscribeButton.setText(R.string.subscribe_button_dialog)
				}
			}
		})
	}

	private fun setSlotTextView() {
		val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(slot.begin)
		Log.d(TAG, "dayOfWeek is $dayOfWeek")
		val day = SimpleDateFormat("dd", Locale.getDefault()).format(slot.begin)
		val month = SimpleDateFormat("MM", Locale.getDefault()).format(slot.begin)
		val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(slot.begin)

		val hourStartTime = SimpleDateFormat("HH", Locale.getDefault()).format(slot.begin)
		val hourStartNoon = SimpleDateFormat("a", Locale.getDefault()).format(slot.begin)

		val hourEndTime = SimpleDateFormat("HH", Locale.getDefault()).format(slot.end)
		val hourEndNoon = SimpleDateFormat("a", Locale.getDefault()).format(slot.end)

		dateTextView.text = getString(R.string.date_text_view, dayOfWeek, day, month, year)
		timeTextView.text = getString(
			R.string.time_text_view,
			hourStartTime,
			hourStartNoon,
			hourEndTime,
			hourEndNoon
		)
		clusterTextView.text = getString(R.string.cluster_text_view, slot.cluster)
		reservedPlacesTextView.text =
			getString(R.string.reserved_places_text_view, slot.reservedPlaces)
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
		reservedPlacesTextView= view.findViewById(R.id.reserved_places_text_view)

		cancelButton.setOnClickListener {
			dismiss()
		}
		subscribeButton.setOnClickListener {
			subscribeDialogViewModel.subscribingToSlot(slot.id)
		}
		return view
	}
}