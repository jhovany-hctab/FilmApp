package org.bedu.filmapp.ui.auth_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthSignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AuthSignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordConfirmInputLayout: TextInputLayout
    private lateinit var signupButton: RelativeLayout
    private lateinit var progressBar: ProgressBar

    private val viewModel by viewModels<AuthSignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameInputLayout = view.findViewById(R.id.name_il)
        emailInputLayout = view.findViewById(R.id.email_il)
        passwordInputLayout = view.findViewById(R.id.password_il)
        passwordConfirmInputLayout = view.findViewById(R.id.password_confirm_il)
        signupButton = view.findViewById(R.id.signup_button_rl)
        progressBar = view.findViewById(R.id.progress_bar)

        nameInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (viewModel.onNameInput(text.toString())) {
                onIsSuccess(nameInputLayout, R.string.auth_signup_name_success)
            } else {
                onIsError(nameInputLayout, R.string.auth_signup_name_error)
            }

        }

        emailInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (viewModel.onEmailInput(text.toString())) {
                onIsSuccess(emailInputLayout, R.string.auth_login_email_success)
            } else {
                onIsError(emailInputLayout, R.string.auth_login_email_error)
            }

        }

        passwordInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (viewModel.onPasswordInput(text.toString())) {
                onIsSuccess(passwordInputLayout, R.string.auth_login_pass_success)
            } else {
                onIsError(passwordInputLayout, R.string.auth_login_pass_error)
            }
        }

        passwordConfirmInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (viewModel.onPasswordConfirm(text.toString())) {
                onIsSuccess(passwordConfirmInputLayout, R.string.auth_signup_pass_confirm_success)
            } else {
                onIsError(passwordConfirmInputLayout, R.string.auth_signup_pass_confirm_error)
            }
        }

        signupButton.setOnClickListener {
            if (viewModel.validateButton()) {
                signupButton.isEnabled = false
                viewModel.onSignup()
            } else {
                Toast.makeText(context, getString(R.string.auth_login_btn_error), Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                viewModel.singUpResponse.collect() {
                    when(it) {
                        Response.Loading -> progressBar.visibility = View.VISIBLE
                        is Response.Success -> viewModel.createUser()//findNavController().navigate(R.id.action_authHomeFragment_to_authLoginFragment)
                        is Response.Failure -> {
                            progressBar.visibility = View.GONE
                            signupButton.isEnabled = true
                            Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
            launch {
                viewModel.createUserResponse.collect() {
                    when(it) {
                        Response.Loading -> progressBar.visibility = View.VISIBLE
                        is Response.Success -> findNavController().navigate(R.id.action_global_homeFragment)
                        is Response.Failure -> {
                            progressBar.visibility = View.GONE
                            signupButton.isEnabled = true
                            Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
        }

    }

    private fun onIsError(
        layout: TextInputLayout,
        @StringRes value: Int
    ) {
        layout.isErrorEnabled = true
        layout.error = getString(value)
    }
    private fun onIsSuccess(
        layout: TextInputLayout,
        @StringRes value: Int
    ) {
        layout.isErrorEnabled = false
        layout.error = null
        layout.helperText = getString(value)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_signup, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthSignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthSignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}