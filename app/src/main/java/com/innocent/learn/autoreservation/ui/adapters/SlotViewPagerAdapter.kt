package com.innocent.learn.autoreservation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.ReservationFragmentDirections

private const val TAG = "SlotViewPagerAdapter"

class SlotViewPagerAdapter(slotDiffCallback: SlotDiffCallback) :
	ListAdapter<Slot, SlotViewPagerAdapter.SlotCardViewHolder>(slotDiffCallback) {

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): SlotCardViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val slotCardViewHolder = layoutInflater.inflate(R.layout.item_view_pager, parent, false)
		return SlotCardViewHolder(slotCardViewHolder)
	}

	override fun onBindViewHolder(holder: SlotCardViewHolder, position: Int) {
		holder.bind(getItem(position))
	}

	class SlotCardViewHolder(item: View) :
		RecyclerView.ViewHolder(item),
		View.OnClickListener {
		private lateinit var slot: Slot

		init {
			item.setOnClickListener(this)
		}

		fun bind(slot: Slot) {
			this.slot = slot
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