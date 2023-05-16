package org.bedu.filmapp.ui.auth_login

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import javax.inject.Inject

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

    private val viewModel by viewModels<AuthLoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailLayout = view.findViewById(R.id.email_il)
        passwordLayout = view.findViewById(R.id.password_il)
        login = view.findViewById(R.id.login_btn_rl)

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
                Toast.makeText(context, "SESION INICIADA", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(R.string.auth_login_btn_error), Toast.LENGTH_SHORT).show()
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