package com.innocent.learn.autoreservation.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.innocent.learn.autoreservation.databinding.FragmentLoginBinding
import com.innocent.learn.autoreservation.network.ReservationResponse
import com.innocent.learn.autoreservation.utils.CookieHelper
import com.innocent.learn.autoreservation.utils.CustomToast

//const val RESERVATION_ID_old = "MTYyMTc0MTQ5OHxEdi1CQkFFQ180SUFBUkFCRUFBQV81bl9nZ0FCQm5OMGNtbHVad3dhQUJoelpYTnphVzl1WDJSaGRHRmZjMlZ6YzJsdmJsOXJaWGthS21Gd2FXbHVkSEpoYjJGMWRHZ3VVMlZ6YzJsdmJrUmhkR0hfZ3dNQkFRdFRaWE56YVc5dVJHRjBZUUhfaEFBQkJRRUdWWE5sY2tsRUFRUUFBUWhEWVcxd2RYTkpSQUVFQUFFSFNYTlRkR0ZtWmdFQ0FBRUpUbVY0ZEVOb1pXTnJBZi1HQUFFRlVtOXNaWE1CXzRvQUFBQVFfNFVGQVFFRVZHbHRaUUhfaGdBQUFDVF9pUUlCQVJWYlhYUnZiMnhwYm5SeVlXOWhkWFJvTGxKdmJHVUJfNG9BQWYtSUFBQWlfNGNEQVFFRVVtOXNaUUhfaUFBQkFnRUNTVVFCQkFBQkJFNWhiV1VCREFBQUFCel9oQmtCX1FISWxnRXFBZzhCQUFBQUR0ZzcxTW95ZUI0bEFBQUF8uOFo-eIJPxaQtDzk3viqU_NqnoYlYg8iR43K6-SlzgA="
const val RESERVATION_ID = "MTYyMjA5MTM1NnxEdi1CQkFFQ180SUFBUkFCRUFBQV81bl9nZ0FCQm5OMGNtbHVad3dhQUJoelpYTnphVzl1WDJSaGRHRmZjMlZ6YzJsdmJsOXJaWGthS21Gd2FXbHVkSEpoYjJGMWRHZ3VVMlZ6YzJsdmJrUmhkR0hfZ3dNQkFRdFRaWE56YVc5dVJHRjBZUUhfaEFBQkJRRUdWWE5sY2tsRUFRUUFBUWhEWVcxd2RYTkpSQUVFQUFFSFNYTlRkR0ZtWmdFQ0FBRUpUbVY0ZEVOb1pXTnJBZi1HQUFFRlVtOXNaWE1CXzRvQUFBQVFfNFVGQVFFRVZHbHRaUUhfaGdBQUFDVF9pUUlCQVJWYlhYUnZiMnhwYm5SeVlXOWhkWFJvTGxKdmJHVUJfNG9BQWYtSUFBQWlfNGNEQVFFRVVtOXNaUUhfaUFBQkFnRUNTVVFCQkFBQkJFNWhiV1VCREFBQUFCel9oQmtCX1FISWxnRXFBZzhCQUFBQUR0aEJLMndEOV9Hc0FBQUF8nwhVOMQln3WaMS4BudloqHxttiN6HW-JXzJNOUOktN4="

class LoginFragment : Fragment() {
	private val viewModel: LoginViewModel by viewModels()
	private lateinit var binding: FragmentLoginBinding
	private var reservationId = CookieHelper.getReservationId()
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//debugging only
		reservationId = RESERVATION_ID
//		CookieHelper.setReservationId("MTYyMTc0MTQ5OHxEdi1CQkFFQ180SUFBUkFCRUFBQV81bl9nZ0FCQm5OMGNtbHVad3dhQUJoelpYTnphVzl1WDJSaGRHRmZjMlZ6YzJsdmJsOXJaWGthS21Gd2FXbHVkSEpoYjJGMWRHZ3VVMlZ6YzJsdmJrUmhkR0hfZ3dNQkFRdFRaWE56YVc5dVJHRjBZUUhfaEFBQkJRRUdWWE5sY2tsRUFRUUFBUWhEWVcxd2RYTkpSQUVFQUFFSFNYTlRkR0ZtWmdFQ0FBRUpUbVY0ZEVOb1pXTnJBZi1HQUFFRlVtOXNaWE1CXzRvQUFBQVFfNFVGQVFFRVZHbHRaUUhfaGdBQUFDVF9pUUlCQVJWYlhYUnZiMnhwYm5SeVlXOWhkWFJvTGxKdmJHVUJfNG9BQWYtSUFBQWlfNGNEQVFFRVVtOXNaUUhfaUFBQkFnRUNTVVFCQkFBQkJFNWhiV1VCREFBQUFCel9oQmtCX1FISWxnRXFBZzhCQUFBQUR0ZzcxTW95ZUI0bEFBQUF8uOFo-eIJPxaQtDzk3viqU_NqnoYlYg8iR43K6-SlzgA=")
//		moveToReservationFragment()
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