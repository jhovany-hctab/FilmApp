package org.bedu.filmapp.ui.auth_signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import org.bedu.filmapp.domain.use_cases.user.UserUseCases
import javax.inject.Inject

@HiltViewModel
class AuthSignupViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases
): ViewModel() {

    //SIGNUP RESPONSE
    var singUpResponse = MutableStateFlow<Response<FirebaseUser>?>(null)
        private set
    //CREATE RESPONSE
    var createUserResponse = MutableStateFlow<Response<Boolean>?>(null)
    var user = User()

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

    fun onSignup() {
        user.username = state.value.name
        user.email = state.value.email
        user.password = state.value.password
        signup(user)
    }

    private fun signup(user: User) = viewModelScope.launch{
        singUpResponse.value = Response.Loading
        val result = authUseCases.authSignup(user)
        singUpResponse.value = result
    }

    fun createUser() = viewModelScope.launch {
        createUserResponse.value = Response.Loading
        user.id = authUseCases.getCurrentUser()!!.uid
        user.imageProfile = "https://netflix-bootcamp-db.netlify.app/static/media/profileIcon1.b36331ae.jpg"
        user.imagePortedProfile = "https://i.pinimg.com/originals/42/ae/da/42aeda1e9c6ce8657c8fc8f152c7eedd.jpg"
        val result = userUseCases.userCreate(user)
        createUserResponse.value = result
    }

}