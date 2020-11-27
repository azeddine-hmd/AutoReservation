package com.innocent.learn.autoreservation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.ReservationFragmentDirections
import java.text.SimpleDateFormat
import java.util.*

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
			val resource = context.resources

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
				resource.getString(R.string.date_text_view, dayOfWeek, day, month, year); timeTextView.text =
				resource.getString(
					R.string.time_text_view,
					hourStartTime,
					hourStartNoon,
					hourEndTime,
					hourEndNoon
				)
			clusterTextView.text = resource.getString(R.string.cluster_text_view, slot.cluster)
			reservedPlacesTextView.text =
				resource.getString(R.string.reserved_places_text_view, slot.reservedPlaces)
		}

		override fun onClick(v: View?) {
			val navController = itemView.findNavController()
			showSubscriptionDialog(navController)
		}

		private fun showSubscriptionDialog(navController: NavController) {
			val action =
				ReservationFragmentDirections.actionReservationFragmentToSubscribeDialog(slot.id)
			navController.navigate(action)
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