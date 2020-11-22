package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.innocent.learn.autoreservation.R

class SettingsFragment : PreferenceFragmentCompat() {

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.settings_preferences, rootKey)
	}
}