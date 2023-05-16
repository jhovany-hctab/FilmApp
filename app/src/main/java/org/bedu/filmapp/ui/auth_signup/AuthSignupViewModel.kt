package org.bedu.filmapp.ui.auth_signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthSignupViewModel @Inject constructor(): ViewModel() {

    var state = MutableStateFlow(AuthSignupState())


    var isNameValid = MutableStateFlow<Boolean?>(null)
    var isEmailValid = MutableStateFlow<Boolean?>(null)
    var isPasswordValid = MutableStateFlow<Boolean?>(null)
    var isPasswordConfirmValid = MutableStateFlow<Boolean?>(null)

    fun onNameInput(name: String): Boolean {
        isNameValid.value = name.length > 5
        state.value = state.value.copy(name =  name)
        return isNameValid.value!!
    }

    fun onEmailInput(email: String): Boolean {
        isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        state.value = state.value.copy(email = email)
        return isEmailValid.value!!
    }

    fun onPasswordInput(password: String): Boolean {
        isPasswordValid.value = password.length > 5
        state.value = state.value.copy(password = password)
        return isPasswordValid.value!!
    }

    fun onPasswordConfirm(passConfirm: String): Boolean {
        isPasswordConfirmValid.value = state.value.password == passConfirm
        state.value = state.value.copy(passwordConfirm = passConfirm)
        return isPasswordConfirmValid.value!!
    }

    fun validateButton(): Boolean{
        return isNameValid.value ?: false &&
                isEmailValid.value ?: false &&
                isPasswordValid.value ?: false &&
                isPasswordConfirmValid.value ?: false
    }

}