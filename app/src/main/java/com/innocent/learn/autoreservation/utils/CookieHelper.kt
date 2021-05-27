package com.innocent.learn.autoreservation.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

private const val COOKIE_RESERVATION_ID= "cookieReservationId"

class CookieHelper private constructor() {
	companion object {
		lateinit var sharedPreferences : SharedPreferences
		
		fun initialize(context: Context) {
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
		}
		
		fun getReservationId(): String {
			return sharedPreferences.getString(COOKIE_RESERVATION_ID, "")!!
		}
		
		fun setReservationId(reservationId: String) {
			sharedPreferences.edit {
				putString(COOKIE_RESERVATION_ID, reservationId)
			}
		}
		
		fun resetReservationId() {
			sharedPreferences.edit {
				putString(COOKIE_RESERVATION_ID, "")
			}
		}
		
	}
}