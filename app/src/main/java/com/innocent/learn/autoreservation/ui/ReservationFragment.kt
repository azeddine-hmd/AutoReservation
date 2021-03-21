package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.adapters.SlotListAdapter
import com.innocent.learn.autoreservation.viewmodel.ReservationFragmentViewModel

private const val TAG = "ReservationFragment"

class ReservationFragment : Fragment() {
	private lateinit var viewModel: ReservationFragmentViewModel
//	private lateinit var adapter: ListAdapter<Slot, SlotListAdapter.SlotViewHolder>
	private lateinit var swipeRefresh: SwipeRefreshLayout


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(this).get(ReservationFragmentViewModel::class.java)
		viewModel.getSlotList.observe(this) { slotList ->
			val filteredSlotList = viewModel.filterSlotListDependsOnTime(slotList)
			updateUI(filteredSlotList)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_reservation, container, false)
		swipeRefresh = view.findViewById(R.id.swipe_refresh)
//		adapter = SlotListAdapter(requireContext(), SlotListAdapter.SlotDiffCallback())
//		view.findViewById<RecyclerView>(R.id.recycler_view).apply {
//			layoutManager = LinearLayoutManager(requireContext())
//			adapter = adapter
//		}
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		swipeRefresh.setOnRefreshListener {
			Log.d(TAG, "setOnRefresh called")
			if (swipeRefresh.isRefreshing) {
				viewModel.fetchSlotList.observe(viewLifecycleOwner) { newSlotList ->
					if (newSlotList.isNotEmpty()) {
						val updatedSlotList = viewModel.updateSlotList(viewModel.slotList, newSlotList)
						// update slot list in database
						viewModel.addSlotList(updatedSlotList)
					}
					// stop swipe refresh icon animation after receiving slot list
					swipeRefresh.isRefreshing = false
				}
			}
		}
	}

	private fun updateUI(slotList: List<Slot>) {
		viewModel.slotList = slotList
//		adapter.submitList(viewModel.slotList)
	}

}