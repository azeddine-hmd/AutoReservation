package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.service.autofill.Validators.not
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.viewmodel.ReservationFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ReservationFragment"

class ReservationFragment : Fragment() {
	private lateinit var swipeRefresh: SwipeRefreshLayout
	private lateinit var reservationFragmentViewModel: ReservationFragmentViewModel
	private lateinit var slotListAdapter: ListAdapter<Slot, SlotViewHolder>
	private lateinit var slotList: List<Slot>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		reservationFragmentViewModel = ViewModelProvider(this)
			.get(ReservationFragmentViewModel::class.java)

		reservationFragmentViewModel.getSlotList.observe(this) { slotList ->
			val filteredSlotList = filterSlotList(slotList)
			updateSlotList(filteredSlotList)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_reservation, container, false)

		swipeRefresh = view.findViewById(R.id.swipe_refresh)
		slotListAdapter = SlotListAdapter(SlotDiffCallback())
		view.findViewById<RecyclerView>(R.id.recycler_view).apply {
			layoutManager = LinearLayoutManager(requireContext())
			adapter = slotListAdapter
		}

		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		swipeRefresh.setOnRefreshListener {
			reservationFragmentViewModel.fetchSlots()
		}
		reservationFragmentViewModel.fetchedSlotList.observe(viewLifecycleOwner) { newSlotList ->
			Log.d(TAG, "items refreshed successfully")

			// update slots in database accordingly
			reservationFragmentViewModel.deleteAllSlots()
			val slotList = updateSlotListWithoutConflict(this.slotList, newSlotList)
			reservationFragmentViewModel.addSlotList(slotList)

			// stop swipe refresh icon
			swipeRefresh.isRefreshing = false
		}
	}

	private fun updateSlotListWithoutConflict(
		oldSlotList: List<Slot>,
		newSlotList: List<Slot>
	): List<Slot> {
		val slotList = mutableListOf<Slot>()

		for (newSlot in newSlotList) {
			for (oldSlot in oldSlotList) {
				if (newSlot.id == oldSlot.id && oldSlot.isInBotList) {
					newSlot.isInBotList = true
					slotList.add(newSlot)
				} else {
					slotList.add(newSlot)
				}
			}
		}

		return slotList.toList()
	}

	private fun filterSlotList(oldSlotList: List<Slot>): List<Slot> {
		val newSlotList = mutableListOf<Slot>()
		val currentDate = Date()


		for (oldSlot in oldSlotList) {
			if (!oldSlot.begin.before(currentDate)) {
				newSlotList.add(oldSlot)
			}
		}

		return newSlotList
	}

	private fun updateSlotList(slotList: List<Slot>) {
		this.slotList = slotList
		slotListAdapter.submitList(this.slotList)
	}


	private inner class SlotViewHolder(item: View) : RecyclerView.ViewHolder(item),
		View.OnClickListener {
		private val dateTextView: TextView = item.findViewById(R.id.date_text_view)
		private val timeTextView: TextView = item.findViewById(R.id.time_text_view)
		private val clusterTextView: TextView = item.findViewById(R.id.cluster_text_view)
		private val reservedPlacesTextView: TextView =
			item.findViewById(R.id.reserved_places_text_view)
		private lateinit var slot: Slot

		init {
			item.setOnClickListener(this)
		}

		fun bind(slot: Slot) {
			this.slot = slot

			val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(slot.begin)
			val day = SimpleDateFormat("dd", Locale.getDefault()).format(slot.begin)
			val month = SimpleDateFormat("MM", Locale.getDefault()).format(slot.begin)
			val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(slot.begin)
			val hourStartTime = SimpleDateFormat("HH", Locale.getDefault()).format(slot.begin)
			val hourStartNoon = SimpleDateFormat("a", Locale.getDefault()).format(slot.begin)
			val hourEndTime = SimpleDateFormat("HH", Locale.getDefault()).format(slot.end)
			val hourEndNoon = SimpleDateFormat("a", Locale.getDefault()).format(slot.end)
			dateTextView.text =
				getString(R.string.date_text_view, dayOfWeek, day, month, year); timeTextView.text =
				getString(
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

		override fun onClick(v: View?) {
			showSubscriptionDialog()
		}

		private fun showSubscriptionDialog() {
			val action =
				ReservationFragmentDirections.actionReservationFragmentToSubscribeDialog(slot.id)
			findNavController().navigate(action)
		}
	}

	private inner class SlotListAdapter(slotDiffCallback: SlotDiffCallback) :
		ListAdapter<Slot, SlotViewHolder>(slotDiffCallback) {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
			val slotViewHolder = layoutInflater.inflate(R.layout.list_item_slot, parent, false)
			return SlotViewHolder(slotViewHolder)
		}

		override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
			holder.bind(getItem(position))
		}
	}

	private inner class SlotDiffCallback : DiffUtil.ItemCallback<Slot>() {

		override fun areItemsTheSame(oldItem: Slot, newItem: Slot): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: Slot, newItem: Slot): Boolean {
			return oldItem == newItem
		}
	}
}