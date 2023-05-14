package org.bedu.filmapp.ui.auth_login

import android.text.Editable
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class AuthLoginViewModel @Inject constructor(): ViewModel() {

    var email : MutableLiveData<String> = MutableLiveData()

    val readEmail: LiveData<String> get() = email

}