package com.innocent.learn.autoreservation.domain.model

data class Page(
	val topLeft: Slot,
	val topRight: Slot,
	val bottomLeft: Slot,
	val bottomRight: Slot,
)