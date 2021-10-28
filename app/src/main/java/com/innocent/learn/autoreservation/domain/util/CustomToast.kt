package com.innocent.learn.autoreservation.domain.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.innocent.learn.autoreservation.R

private const val X_OFFEST = 0
private const val Y_OFFEST = 64

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

	fun showError(context: Context, errorMessage: String) {
		val errorToast = Toast(context)
		val layoutInflater = LayoutInflater.from(context)

		val customToastView = layoutInflater.inflate(R.layout.custom_toast, null) as TextView
		customToastView.text = errorMessage
		errorToast.view = customToastView

		errorToast.setGravity(Gravity.TOP, X_OFFEST, Y_OFFEST)

		errorToast.show()
	}

	fun showSuccess(context: Context, successMessageId: Int) {
		val successToast = Toast(context)
		val layoutInflater = LayoutInflater.from(context)

		val customToastView = layoutInflater.inflate(R.layout.custom_toast, null) as TextView
		customToastView.background = context.resources.getDrawable(R.drawable.custum_toast_success, context.theme)
		customToastView.setText(successMessageId)
		successToast.view = customToastView

		successToast.setGravity(Gravity.TOP, X_OFFEST, Y_OFFEST)

		successToast.show()
	}

	fun showSuccess(context: Context, successMessage: String) {
		val successToast = Toast(context)
		val layoutInflater = LayoutInflater.from(context)

		val customToastView = layoutInflater.inflate(R.layout.custom_toast, null) as TextView
		customToastView.background = context.resources.getDrawable(R.drawable.custum_toast_success, context.theme)
		customToastView.text = successMessage
		successToast.view = customToastView

		successToast.setGravity(Gravity.TOP, X_OFFEST, Y_OFFEST)

		successToast.show()
	}

}