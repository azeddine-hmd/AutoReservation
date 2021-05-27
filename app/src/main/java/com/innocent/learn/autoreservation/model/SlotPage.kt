package com.innocent.learn.autoreservation.model

data class SlotPage(
	val topLeft: Slot,
	val topRight: Slot,
	val bottomLeft: Slot,
	val bottomRight: Slot,
)