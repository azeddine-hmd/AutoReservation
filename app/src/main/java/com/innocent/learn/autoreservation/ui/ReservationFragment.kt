package com.innocent.learn.autoreservation.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.viewmodel.ReservationFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ReservationFragment"

class ReservationFragment : Fragment() {
	private lateinit var reservationFragmentViewModel: ReservationFragmentViewModel
	private lateinit var slotListAdapter: ListAdapter<Slot, SlotViewHolder>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		reservationFragmentViewModel =
			ViewModelProvider(this).get(ReservationFragmentViewModel::class.java)
	}

	@RequiresApi(Build.VERSION_CODES.N)
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_reservation, container, false)
		slotListAdapter = SlotListAdapter(SlotDiffCallback())
		view.findViewById<RecyclerView>(R.id.recycler_view).apply {
			layoutManager = LinearLayoutManager(requireContext())
			adapter = slotListAdapter
		}
		reservationFragmentViewModel.slotsLiveData.observe(
			viewLifecycleOwner, { slotList ->
				val mutableSlotList = slotList.toMutableList()
				val currentDate = Date()
				mutableSlotList.removeIf { it.begin.before(currentDate) }
				slotListAdapter.submitList(mutableSlotList)
				reservationFragmentViewModel.addSlotList(slotList)
			}
		)
		reservationFragmentViewModel.fetchSlots()
		return view
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

		override fun onClick(v: View?) {
			Log.d(TAG, "onClick: ")
			val action = ReservationFragmentDirections.actionReservationFragmentToSubscribeDialog(slot.id)
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
