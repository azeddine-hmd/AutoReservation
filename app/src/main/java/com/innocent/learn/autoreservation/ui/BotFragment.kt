package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.viewmodel.BotFragmentViewModel

private const val TAG = "BotFragment"

class BotFragment : Fragment() {
	private lateinit var recyclerView: RecyclerView
	private lateinit var startBotButton: Button
	private lateinit var botSlotList: List<Slot>
	private lateinit var viewModel: BotFragmentViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this).get(BotFragmentViewModel::class.java)
		viewModel.botSlotLiveData.observe(this) { slotList ->
			val mutableSlotList = mutableListOf<Slot>()
			for (slot in slotList) {
				if (slot.isInBotList) {
					mutableSlotList.add(slot)
				}
			}
			botSlotList = mutableSlotList.toList()
			Log.d(TAG, "$botSlotList")
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_bot, container, false)
		recyclerView = view.findViewById(R.id.recycler_view)
		startBotButton = view.findViewById(R.id.bot_start_button)
		return view
	}
}