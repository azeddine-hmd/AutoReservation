package com.innocent.learn.autoreservation.presentation.ui.reservation.adapter

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
import com.innocent.learn.autoreservation.domain.model.Page
import com.innocent.learn.autoreservation.domain.model.Slot
import com.innocent.learn.autoreservation.presentation.ui.reservation.ReservationFragmentDirections
import java.util.Date

private const val TAG = "SlotViewPagerAdapter"

class SlotViewPagerAdapter() : RecyclerView.Adapter<SlotViewPagerAdapter.SlotCardViewHolder>() {
	var pageList: List<Page> = emptyList()

	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): SlotCardViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		val slotCardViewHolder = layoutInflater.inflate(R.layout.item_view_pager, parent, false)
		return SlotCardViewHolder(slotCardViewHolder)
	}

	override fun onBindViewHolder(holder: SlotCardViewHolder, position: Int) {
		holder.bind(pageList[position])
	}

	override fun getItemCount(): Int = pageList.size

	fun submitList(pageList: List<Page>) {
		this.pageList = pageList
		notifyDataSetChanged()
	}

	class SlotCardViewHolder(item: View) :
		RecyclerView.ViewHolder(item) {
		private lateinit var page: Page
		private val morningLeftCardView: CardView = item.findViewById(R.id.morning_slot_left)
		private val morningRightCardView: CardView = item.findViewById(R.id.morning_slot_right)
		private val afternoonLeftCardView: CardView = item.findViewById(R.id.afternoon_slot_left)
		private val afternoonRightCardView: CardView = item.findViewById(R.id.afternoon_slot_right)

		init {
			setCardsListener()
		}

		fun bind(page: Page) {
			this.page = page
			setIndicators()
			setReservedTexts()
		}

		private fun setCardsListener() {
			morningLeftCardView.setOnClickListener {
				showSubscriptionDialog(page.topLeft)
			}
			morningRightCardView.setOnClickListener {
				showSubscriptionDialog(page.topRight)
			}
			afternoonLeftCardView.setOnClickListener {
				showSubscriptionDialog(page.bottomLeft)
			}
			afternoonRightCardView.setOnClickListener {
				showSubscriptionDialog(page.bottomRight)
			}
		}

		private fun setIndicators() {
			setIndicatorDrawable(page.topLeft, morningLeftCardView.findViewById(R.id.indicator))
			setIndicatorDrawable(
				page.topRight,
				morningRightCardView.findViewById(R.id.indicator)
			)
			setIndicatorDrawable(
				page.bottomLeft,
				afternoonLeftCardView.findViewById(R.id.indicator)
			)
			setIndicatorDrawable(
				page.bottomRight,
				afternoonRightCardView.findViewById(R.id.indicator)
			)
		}

		private fun setReservedTexts() {
			setReservedSlots(
				page.topLeft,
				morningLeftCardView.findViewById(R.id.reserved_slots)
			)
			setReservedSlots(
				page.topRight,
				morningRightCardView.findViewById(R.id.reserved_slots)
			)
			setReservedSlots(
				page.bottomLeft,
				afternoonLeftCardView.findViewById(R.id.reserved_slots)
			)
			setReservedSlots(
				page.bottomRight,
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

	class SlotDiffCallback : DiffUtil.ItemCallback<Page>() {

		override fun areItemsTheSame(oldItem: Page, newItem: Page): Boolean {
			return oldItem == newItem
		}

		override fun areContentsTheSame(oldItem: Page, newItem: Page): Boolean {
			return oldItem == newItem
		}
	}

}