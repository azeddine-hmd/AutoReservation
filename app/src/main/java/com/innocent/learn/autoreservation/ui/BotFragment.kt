package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.adapters.BotListAdapter
import com.innocent.learn.autoreservation.ui.adapters.SlotListAdapter
import com.innocent.learn.autoreservation.viewmodel.BotFragmentViewModel

private const val TAG = "BotFragment"

class BotFragment : Fragment() {
	private lateinit var viewModel: BotFragmentViewModel
	private lateinit var botListAdapter: ListAdapter<Slot, BotListAdapter.BotViewHolder>
	private lateinit var startBotButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this).get(BotFragmentViewModel::class.java)
		viewModel.botListLiveData.observe(this) { botList ->
			Log.d(TAG, "botList: $botList")
			updateUI(botList)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_bot, container, false)
		botListAdapter = BotListAdapter(requireContext(), BotListAdapter.BotDiffCallback())
		view.findViewById<RecyclerView>(R.id.recycler_view).apply {
			layoutManager = LinearLayoutManager(requireContext())
			adapter = botListAdapter
		}
		startBotButton = view.findViewById(R.id.bot_start_button)
		return view
	}

	private fun updateUI(botList: List<Slot>) {
		viewModel.botList = botList
		botListAdapter.submitList(botList)
	}

}