package org.bedu.filmapp.ui.auth_login

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
import androidx.navigation.findNavController
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
 * Use the [AuthLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class AuthLoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var login: RelativeLayout
    private lateinit var progressBar: ProgressBar

    private val viewModel by viewModels<AuthLoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailLayout = view.findViewById(R.id.email_il)
        passwordLayout = view.findViewById(R.id.password_il)
        login = view.findViewById(R.id.login_btn_rl)
        progressBar = view.findViewById(R.id.progress_bar)
        

        emailLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (viewModel.onEmailInput(text.toString()))
                onIsSuccess(emailLayout, R.string.auth_login_email_success)
            else
                onIsError(emailLayout, R.string.auth_login_email_error)
        }

        passwordLayout.editText?.doOnTextChanged { text, start, before, count ->
            if (viewModel.onPasswordInput(text.toString()))
                onIsSuccess(passwordLayout, R.string.auth_login_pass_success)
            else
                onIsError(passwordLayout, R.string.auth_login_pass_error)
        }

        login.setOnClickListener {
            if (viewModel.validateButton()) {
                login.isEnabled = false
                viewModel.login()
            } else {
                Toast.makeText(context, getString(R.string.auth_login_btn_error), Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.loginResponse.collect() {
                when(it) {
                    Response.Loading -> progressBar.visibility = View.VISIBLE
                    is Response.Success -> findNavController().navigate(R.id.action_global_homeFragment)
                    is Response.Failure -> {
                        progressBar.visibility = View.GONE
                        login.isEnabled = true
                        Toast.makeText(context, it.e.message ?: getString(R.string.auth_login_error), Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
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
        return inflater.inflate(R.layout.fragment_auth_login, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthLoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}