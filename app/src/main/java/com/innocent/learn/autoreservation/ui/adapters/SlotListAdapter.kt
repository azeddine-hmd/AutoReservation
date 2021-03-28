package com.innocent.learn.autoreservation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.ReservationFragmentDirections
import java.text.SimpleDateFormat
import java.util.Locale

private const val TAG = "SlotListAdapter"

class SlotListAdapter(private val context: Context, slotDiffCallback: SlotDiffCallback) :
	ListAdapter<Slot, SlotListAdapter.SlotViewHolder>(slotDiffCallback) {

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): SlotViewHolder {
		val layoutInflater = LayoutInflater.from(context)
		val slotViewHolder = layoutInflater.inflate(R.layout.list_item_slot, parent, false)
		return SlotViewHolder(context, slotViewHolder)
	}

	override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class SlotViewHolder(private val context: Context, item: View) : RecyclerView.ViewHolder(item),
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
			val date: String =
				SimpleDateFormat("EEE dd-MM-yyyy", Locale.getDefault()).format(slot.begin)
			val timeBegin: String = SimpleDateFormat("HHa", Locale.getDefault()).format(slot.begin)
			val timeEnd: String = SimpleDateFormat("HHa", Locale.getDefault()).format(slot.end)

			context.resources.apply {
				dateTextView.text = getString(R.string.list_item_date, date)
				timeTextView.text = getString(R.string.list_item_time, timeBegin, timeEnd)
				clusterTextView.text = getString(R.string.list_item_cluster, slot.cluster)
				reservedPlacesTextView.text =
					getString(R.string.list_item_reserved, slot.reservedPlaces)
			}
		}

		override fun onClick(v: View?) {
			showSubscriptionDialog()
		}

		private fun showSubscriptionDialog() {
			val action =
				ReservationFragmentDirections.actionReservationFragmentToSubscribeDialog(slot.id)
			itemView.findNavController().navigate(action)
		}

	}

	class SlotDiffCallback : DiffUtil.ItemCallback<Slot>() {

		override fun areItemsTheSame(oldItem: Slot, newItem: Slot): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: Slot, newItem: Slot): Boolean {
			return oldItem == newItem
		}
	}

}