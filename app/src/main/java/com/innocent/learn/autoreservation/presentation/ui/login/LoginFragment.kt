package com.innocent.learn.autoreservation.presentation.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.innocent.learn.autoreservation.databinding.FragmentLoginBinding
import com.innocent.learn.autoreservation.network.response.ReservationResponse
import com.innocent.learn.autoreservation.domain.util.CookieHelper
import com.innocent.learn.autoreservation.domain.util.CustomToast

private const val RESERVATION_ID = "MTYzNTM3OTYzNnxEdi1CQkFFQ180SUFBUkFCRUFBQV81bl9nZ0FCQm5OMGNtbHVad3dhQUJoelpYTnphVzl1WDJSaGRHRmZjMlZ6YzJsdmJsOXJaWGthS21Gd2FXbHVkSEpoYjJGMWRHZ3VVMlZ6YzJsdmJrUmhkR0hfZ3dNQkFRdFRaWE56YVc5dVJHRjBZUUhfaEFBQkJRRUdWWE5sY2tsRUFRUUFBUWhEWVcxd2RYTkpSQUVFQUFFSFNYTlRkR0ZtWmdFQ0FBRUpUbVY0ZEVOb1pXTnJBZi1HQUFFRlVtOXNaWE1CXzRvQUFBQVFfNFVGQVFFRVZHbHRaUUhfaGdBQUFDVF9pUUlCQVJWYlhYUnZiMnhwYm5SeVlXOWhkWFJvTGxKdmJHVUJfNG9BQWYtSUFBQWlfNGNEQVFFRVVtOXNaUUhfaUFBQkFnRUNTVVFCQkFBQkJFNWhiV1VCREFBQUFCel9oQmtCX1FISWxnRXFBZzhCQUFBQUR0a0w3c1FhZGdtOEFBQUF8sp0frDjFtYClVpmTHMnKdovpT-c7B4jizgzoPbgZdNo="

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {
	private val viewModel: LoginViewModel by viewModels()
	private lateinit var binding: FragmentLoginBinding
	private var reservationId = CookieHelper.getReservationId()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		// debugging only
		reservationId = RESERVATION_ID
		CookieHelper.setReservationId(reservationId)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentLoginBinding.inflate(inflater)
		return binding.root
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.reservationEditText.setText(reservationId)
		binding.reservationEditText.addTextChangedListener(object : TextWatcher {
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			override fun afterTextChanged(s: Editable?) {}
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				reservationId = s.toString()
			}
		})
		
		binding.loginButton.setOnClickListener {
			viewModel.loginButtonClicked().observe(viewLifecycleOwner) { response ->
				when (response) {
					is ReservationResponse.Success -> {
						Log.d(TAG, "onViewCreated: response was successful")
						moveToReservationFragment()
					}
					is ReservationResponse.Failure -> {
						response.error.message?.let {
							CustomToast.showError(requireContext(), it)
						}
					}
				}
			}
		}
		
	}
	
	private fun moveToReservationFragment() {
		val action = LoginFragmentDirections.actionLoginFragmentToReservationFragment()
		findNavController().navigate(action)
	}
	
}