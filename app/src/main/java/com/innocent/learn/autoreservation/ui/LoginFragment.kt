package com.innocent.learn.autoreservation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.innocent.learn.autoreservation.R
import com.innocent.learn.autoreservation.utils.CookiePreference
import com.innocent.learn.autoreservation.utils.CustomToast
import com.innocent.learn.autoreservation.viewmodel.LoginFragmentViewModel

class LoginFragment : Fragment() {
	private lateinit var viewModel: LoginFragmentViewModel
	private lateinit var cookieEditText: EditText
	private lateinit var loginButton: Button
	private lateinit var reservationId: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProvider(requireActivity()).get(LoginFragmentViewModel::class.java)
		//reservationId = CookiePreference.getStoredReservationId(requireContext())

		// only for debugging purposes
		reservationId = "MTYxNzc1ODMzM3xEdi1CQkFFQ180SUFBUkFCRUFBQV81bl9nZ0FCQm5OMGNtbHVad3dhQUJoelpYTnphVzl1WDJSaGRHRmZjMlZ6YzJsdmJsOXJaWGthS21Gd2FXbHVkSEpoYjJGMWRHZ3VVMlZ6YzJsdmJrUmhkR0hfZ3dNQkFRdFRaWE56YVc5dVJHRjBZUUhfaEFBQkJRRUdWWE5sY2tsRUFRUUFBUWhEWVcxd2RYTkpSQUVFQUFFSFNYTlRkR0ZtWmdFQ0FBRUpUbVY0ZEVOb1pXTnJBZi1HQUFFRlVtOXNaWE1CXzRvQUFBQVFfNFVGQVFFRVZHbHRaUUhfaGdBQUFDVF9pUUlCQVJWYlhYUnZiMnhwYm5SeVlXOWhkWFJvTGxKdmJHVUJfNG9BQWYtSUFBQWlfNGNEQVFFRVVtOXNaUUhfaUFBQkFnRUNTVVFCQkFBQkJFNWhiV1VCREFBQUFCel9oQmtCX1FISWxnRXFBZzhCQUFBQUR0Zl9EWTA2TFZYckFBQUF837gupivgvgT3YEqjqbQ5r1mZWmaczPhY9bTN5qUEnVo="

		//moveToReservationFragment()
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
		loginButton.setOnClickListener {
			if (viewModel.isValidReservationId(reservationId)) {
				viewModel.fetchSlotList(reservationId).observe(viewLifecycleOwner) { slotList ->
					viewModel.addSlotList(slotList)
					moveToReservationFragment()
				}
			} else {
				CustomToast.showError(requireContext(), R.string.invalid_reservation_id)
			}
		}
	}

	private fun moveToReservationFragment() {
		CookiePreference.setReservationId(requireContext(), reservationId)
		val action = LoginFragmentDirections.actionLoginFragmentToReservationFragment()
		findNavController().navigate(action)
	}

}