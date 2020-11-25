package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.utils.CookiePreference
import com.innocent.learn.autoreservation.viewmodel.LoginFragmentViewModel

class LoginFragment : Fragment() {
	private lateinit var loginFragmentViewModel: LoginFragmentViewModel
	private lateinit var cookieEditText: EditText
	private lateinit var loginButton: Button
	private lateinit var reservationId: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		loginFragmentViewModel = ViewModelProvider(this).get(LoginFragmentViewModel::class.java)
		reservationId = CookiePreference.getStoredReservationId(requireContext())
		if (loginFragmentViewModel.isValideReservationId(reservationId)) {
			moveToNextFragment()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val view = inflater.inflate(R.layout.fragment_login, container, false)
		cookieEditText = view.findViewById(R.id.cookie_number_edittext)
		cookieEditText.setText(reservationId)
		loginButton = view.findViewById(R.id.login_button)
		loginButton.setOnClickListener {
			if (loginFragmentViewModel.isValideReservationId(reservationId)) {
				loginFragmentViewModel.fetchSlots(reservationId)
			} else {
				val toast = Toast.makeText(
					requireContext(),
					R.string.invalid_reservation_id,
					Toast.LENGTH_SHORT
				)
				toast.setGravity(Gravity.TOP, 0, 10)
				toast.show()
			}
		}
		loginFragmentViewModel.slotListLiveData.observe(
			viewLifecycleOwner
		) { slotList ->
			CookiePreference.setReservationId(requireContext(), reservationId)
			loginFragmentViewModel.addSlotList(slotList)
			moveToNextFragment()
		}
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		cookieEditText.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun afterTextChanged(s: Editable?) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				reservationId = s.toString()
			}
		})
	}

	private fun moveToNextFragment() {
		val action = LoginFragmentDirections.actionLoginFragmentToReservationFragment()
		findNavController().navigate(action)
	}
}