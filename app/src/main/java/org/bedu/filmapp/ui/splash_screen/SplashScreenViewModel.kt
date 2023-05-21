package org.bedu.filmapp.ui.splash_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val authUseCases: AuthUseCases): ViewModel() {

    var sesionResponse = MutableStateFlow(false)
    private val currentUser = authUseCases.getCurrentUser()!!.uid

    init {
        if (currentUser != null) {
            sesionResponse.value = true
        }
    }

}