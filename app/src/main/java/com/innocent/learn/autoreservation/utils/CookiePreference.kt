package com.innocent.learn.autoreservation.utils

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val PREF_COOKIE_RESERVATION_ID = "cookieReservationId"

object CookiePreference {

	fun getStoredReservationId(context: Context): String {
		val prefs = PreferenceManager.getDefaultSharedPreferences(context)
		return prefs.getString(PREF_COOKIE_RESERVATION_ID, "")!!
	}

	fun setReservationId(context: Context, reservationId: String) {
		PreferenceManager.getDefaultSharedPreferences(context).edit {
			putString(PREF_COOKIE_RESERVATION_ID, reservationId)
		}
	}
}