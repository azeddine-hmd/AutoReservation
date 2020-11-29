package com.innocent.learn.autoreservation.utils

import android.content.Context

class CookieHelper {

	companion object {

		fun getReservationCookie(context: Context): String {
			val reservationId = CookiePreference.getStoredReservationId(context)
			return "reservation_system=$reservationId"
		}

		fun getReservationCookie(reservationId: String): String {
			return "reservation_system=$reservationId"
		}

	}

}