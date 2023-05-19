package org.bedu.filmapp.ui.auth_login

import android.text.Editable
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class AuthLoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
): ViewModel() {

    //login response
    var loginResponse = MutableStateFlow<Response<FirebaseUser>?>(null)

    var state = MutableStateFlow(AuthLoginState())
        private set
    var isEmailValid = MutableStateFlow<Boolean?>(null)
        private set

    var isPasswordValid = MutableStateFlow<Boolean?>(null)
        private set

    fun validateButton(): Boolean{
        return isEmailValid.value ?: false && isPasswordValid.value ?: false
    }
    fun onEmailInput(email: String): Boolean {
        state.value = state.value.copy(email = email)
        isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return isEmailValid.value!!
    }
    fun onPasswordInput(password: String): Boolean {
        state.value = state.value.copy(password = password)
        isPasswordValid.value = password.length>6
        return isPasswordValid.value!!
    }

    fun login() = viewModelScope.launch {
        loginResponse.value = Response.Loading
        val result = authUseCases.authLogin(state.value.email, state.value.password)
        loginResponse.value = result
    }

}