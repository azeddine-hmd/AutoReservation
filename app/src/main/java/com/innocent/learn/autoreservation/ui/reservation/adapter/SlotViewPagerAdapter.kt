package com.innocent.learn.autoreservation.ui.reservation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.model.SlotPage
import com.innocent.learn.autoreservation.model.Slot
import com.innocent.learn.autoreservation.ui.reservation.ReservationFragmentDirections
import java.util.Date

private const val TAG = "SlotViewPagerAdapter"

class SlotViewPagerAdapter() : RecyclerView.Adapter<SlotViewPagerAdapter.SlotCardViewHolder>() {
	var slotPageList: List<SlotPage> = emptyList()

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): SlotCardViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val slotCardViewHolder = layoutInflater.inflate(R.layout.item_view_pager, parent, false)
		return SlotCardViewHolder(slotCardViewHolder)
	}

	override fun onBindViewHolder(holder: SlotCardViewHolder, position: Int) {
		holder.bind(slotPageList[position])
	}

	override fun getItemCount(): Int = slotPageList.size

	fun submitList(slotPageList: List<SlotPage>) {
		this.slotPageList = slotPageList
		notifyDataSetChanged()
	}

	class SlotCardViewHolder(item: View) :
		RecyclerView.ViewHolder(item) {
		private lateinit var pageSlot: SlotPage
		private val morningLeftCardView: CardView = item.findViewById(R.id.morning_slot_left)
		private val morningRightCardView: CardView = item.findViewById(R.id.morning_slot_right)
		private val afternoonLeftCardView: CardView = item.findViewById(R.id.afternoon_slot_left)
		private val afternoonRightCardView: CardView = item.findViewById(R.id.afternoon_slot_right)

		init {
			setCardsListener()
		}

		fun bind(pageSlot: SlotPage) {
			this.pageSlot = pageSlot
			setIndicators()
			setReservedTexts()
		}

		private fun setCardsListener() {
			morningLeftCardView.setOnClickListener {
				showSubscriptionDialog(pageSlot.topLeft)
			}
			morningRightCardView.setOnClickListener {
				showSubscriptionDialog(pageSlot.topRight)
			}
			afternoonLeftCardView.setOnClickListener {
				showSubscriptionDialog(pageSlot.bottomLeft)
			}
			afternoonRightCardView.setOnClickListener {
				showSubscriptionDialog(pageSlot.bottomRight)
			}
		}

		private fun setIndicators() {
			setIndicatorDrawable(pageSlot.topLeft, morningLeftCardView.findViewById(R.id.indicator))
			setIndicatorDrawable(
				pageSlot.topRight,
				morningRightCardView.findViewById(R.id.indicator)
			)
			setIndicatorDrawable(
				pageSlot.bottomLeft,
				afternoonLeftCardView.findViewById(R.id.indicator)
			)
			setIndicatorDrawable(
				pageSlot.bottomRight,
				afternoonRightCardView.findViewById(R.id.indicator)
			)
		}

		private fun setReservedTexts() {
			setReservedSlots(
				pageSlot.topLeft,
				morningLeftCardView.findViewById(R.id.reserved_slots)
			)
			setReservedSlots(
				pageSlot.topRight,
				morningRightCardView.findViewById(R.id.reserved_slots)
			)
			setReservedSlots(
				pageSlot.bottomLeft,
				afternoonLeftCardView.findViewById(R.id.reserved_slots)
			)
			setReservedSlots(
				pageSlot.bottomRight,
				afternoonRightCardView.findViewById(R.id.reserved_slots)
			)
		}


		private fun setReservedSlots(slot: Slot, reservedSlotsTextView: TextView) {
			val reservedString =
				itemView.context.getString(R.string.reserved_places, slot.reservedPlaces)
			reservedSlotsTextView.text = reservedString
		}

		private fun setIndicatorDrawable(slot: Slot, indicatorImageView: ImageView) {
			val drawable: Drawable?
			drawable = if (slot.begin.before(Date())) {
				ContextCompat.getDrawable(itemView.context, R.drawable.bg_outaded_slot)
			} else if (slot.isSubscribed) {
				ContextCompat.getDrawable(itemView.context, R.drawable.bg_available_slot)
			} else {
				ContextCompat.getDrawable(itemView.context, R.drawable.bg_reserved_slot)
			}

			indicatorImageView.setImageDrawable(drawable)
		}

		private fun showSubscriptionDialog(slot: Slot) {
			val action =
				ReservationFragmentDirections.actionReservationFragmentToSubscribeDialog(
					slot.id,
					adapterPosition
				)
			itemView.findNavController().navigate(action)
		}

	}

	class SlotDiffCallback : DiffUtil.ItemCallback<SlotPage>() {

		override fun areItemsTheSame(oldItem: SlotPage, newItem: SlotPage): Boolean {
			return oldItem == newItem
		}

		override fun areContentsTheSame(oldItem: SlotPage, newItem: SlotPage): Boolean {
			return oldItem == newItem
		}
	}

}