package com.innocent.learn.autoreservation.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.innocent.learn.autoreservation.R

object CustomToast {

	fun showError(context: Context, errorMessageId: Int) {
		val errorToast = Toast(context)
		val layoutInflater = LayoutInflater.from(context)

		val customToastView = layoutInflater.inflate(R.layout.custom_toast, null) as TextView
		customToastView.setText(errorMessageId)
		errorToast.view = customToastView

		errorToast.setGravity(Gravity.TOP, 0, 64)

		errorToast.show()
	}

}