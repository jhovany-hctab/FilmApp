package org.bedu.filmapp.ui.auth_login

import android.text.Editable
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.bedu.filmapp.R
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class AuthLoginViewModel @Inject constructor(): ViewModel() {

    var isEmailValid = MutableStateFlow<Boolean?>(null)
        private set

    var isPasswordValid = MutableStateFlow<Boolean?>(null)
        private set

    fun validateButton(): Boolean{
        return isEmailValid.value ?: false && isPasswordValid.value ?: false
    }
    fun onEmailInput(email: String): Boolean {
        isEmailValid.value = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return isEmailValid.value!!
    }
    fun onPasswordInput(password: String): Boolean {
        isPasswordValid.value = password.length>6
        return isPasswordValid.value!!
    }

}