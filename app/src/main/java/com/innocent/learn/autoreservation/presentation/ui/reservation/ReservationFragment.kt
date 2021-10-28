package com.innocent.learn.autoreservation.presentation.ui.reservation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.databinding.FragmentReservationBinding
import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.network.response.ReservationResponse
import com.innocent.learn.autoreservation.presentation.ui.reservation.adapter.SlotViewPagerAdapter
import com.innocent.learn.autoreservation.domain.util.CustomToast

private const val TAG = "ReservationFragment"

class ReservationFragment : Fragment() {
	private val viewModel: ReservationViewModel by viewModels()
	private lateinit var binding: FragmentReservationBinding
	private val adapter = SlotViewPagerAdapter()
	private var currentPosition = 0
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentReservationBinding.inflate(layoutInflater)
		binding.slotsPager.adapter = adapter
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		viewModel.getSlotList().observe(viewLifecycleOwner) { newSlotList ->
			updateUI(newSlotList)
		}
		
		findNavController().addOnDestinationChangedListener { _, destination, _ ->
			if (destination.id == R.id.reservation_fragment) {
				changeTitle()
			}
		}
		
		binding.slotsPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				Log.d(TAG, "onPageSelected: $position")
				currentPosition = position
				changeTitle()
			}
			
		})
		
		binding.swipeRefresh.setOnRefreshListener {
			if (!binding.swipeRefresh.isRefreshing) {
				viewModel.swipeRefreshTriggered().observe(viewLifecycleOwner) { response ->
					when (response) {
						is ReservationResponse.Success -> {
							Log.d(TAG, "received new slot list from net: ${response.data}")
							updateUI(response.data)
						}
						is ReservationResponse.Failure -> {
							response.error.message?.let {
								CustomToast.showError(requireContext(), it)
							}
						}
					}
					binding.swipeRefresh.isRefreshing = false
				}
			}
		}
		
	}
	
	private fun updateUI(slotList: List<Slot>) {
		val pageList = viewModel.pageListConverter(slotList)
		adapter.submitList(pageList)
	}
	
	private fun changeTitle() {
		(requireActivity() as AppCompatActivity).supportActionBar?.title = "page $currentPosition"
	}
	
}