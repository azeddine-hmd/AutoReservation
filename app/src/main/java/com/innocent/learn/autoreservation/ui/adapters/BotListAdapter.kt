package com.innocent.learn.autoreservation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.BotFragmentDirections
import com.innocent.learn.autoreservation.ui.ReservationFragmentDirections
import com.innocent.learn.autoreservation.utils.CustomToast
import java.text.SimpleDateFormat
import java.util.Locale

class BotListAdapter(private val context: Context, botDiffCallback: BotDiffCallback)
	: ListAdapter<Slot, BotListAdapter.BotViewHolder>(botDiffCallback) {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotViewHolder {
		val layoutInflater = LayoutInflater.from(context)
		val botViewholder = layoutInflater.inflate(R.layout.list_item_bot, parent, false)
		return BotViewHolder(context, botViewholder)
	}

	override fun onBindViewHolder(holder: BotViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class BotViewHolder(private val context: Context, item: View)
		: RecyclerView.ViewHolder(item), View.OnClickListener {
		private val dateTextView: TextView = item.findViewById(R.id.date)
		private val timeTextView: TextView = item.findViewById(R.id.time)
		private val clusterTextView: TextView = item.findViewById(R.id.cluster)
		private val reservedPlacesTextView: TextView =
			item.findViewById(R.id.reserved)
		private val removeButton: ImageButton = item.findViewById(R.id.remove_button)
		private lateinit var slot: Slot

		init {
			item.setOnClickListener(this)
			removeButton.setOnClickListener {
				CustomToast.showSuccess(context, "Minus Button Clicked!")
			}
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
			val action = BotFragmentDirections.actionBotFragmentToSubscribeDialog(slot.id)
			itemView.findNavController().navigate(action)
		}
	}

	class BotDiffCallback() : DiffUtil.ItemCallback<Slot>() {

		override fun areItemsTheSame(oldItem: Slot, newItem: Slot): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: Slot, newItem: Slot): Boolean {
			return oldItem == newItem
		}
	}

}