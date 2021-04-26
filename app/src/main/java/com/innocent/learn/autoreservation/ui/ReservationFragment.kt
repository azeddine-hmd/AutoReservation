package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.PageSlot
import com.innocent.learn.autoreservation.ui.adapters.SlotViewPagerAdapter
import com.innocent.learn.autoreservation.viewmodel.ReservationFragmentViewModel
import kotlinx.android.synthetic.main.activity_main.toolbar

private const val TAG = "ReservationFragment"

class ReservationFragment : Fragment() {
	private lateinit var viewModel: ReservationFragmentViewModel
	private lateinit var adapter: ListAdapter<PageSlot, SlotViewPagerAdapter.SlotCardViewHolder>
	private lateinit var swipeRefresh: SwipeRefreshLayout
	private lateinit var viewPager: ViewPager2


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(requireActivity()).get(ReservationFragmentViewModel::class.java)
		viewModel.getSlotList.observe(this) { newSlotList ->
			viewModel.slotList = newSlotList.toMutableList()
			updateUI()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_reservation, container, false)
		swipeRefresh = view.findViewById(R.id.swipe_refresh)

		adapter = SlotViewPagerAdapter(SlotViewPagerAdapter.SlotDiffCallback())

		viewPager = view.findViewById(R.id.slots_pager)
		viewPager.adapter = adapter
		viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				Log.d(TAG, "onPageSelected: $position")
				viewModel.position = position
				changeTitle()
			}
		})

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		findNavController().addOnDestinationChangedListener { _ , destination, _ ->
			if (destination.id == R.id.reservation_fragment) {
				changeTitle()
			}
		}

		swipeRefresh.setOnRefreshListener {
			viewModel.position = viewPager.currentItem
			if (swipeRefresh.isRefreshing) {
				viewModel.fetchSlotList.observe(viewLifecycleOwner) { newSlotList ->
					if (newSlotList.isNotEmpty()) {
						Log.d(TAG, "received new slot list from net: $newSlotList")
						viewModel.slotList = newSlotList.toMutableList()
						viewModel.addSlotList(newSlotList)
						updateUI()
					}
					// stop swipe refresh icon animation after receiving slot list
					swipeRefresh.isRefreshing = false
				}
			}
		}

		viewModel.updateViewPager.observe(viewLifecycleOwner, { position ->
			Log.d(TAG, "position to update $position")
			viewModel.position = position
			updateUI()
		})


	}

	private fun changeTitle() {
		requireActivity().toolbar.title = "Page: ${viewModel.position}"
	}

	private fun updateUI() {
		val pageSlotList = viewModel.convertSlotList(viewModel.slotList)
		adapter.submitList(pageSlotList) {
			viewPager.post {
				viewPager.currentItem = viewModel.position
			}
		}
	}

}