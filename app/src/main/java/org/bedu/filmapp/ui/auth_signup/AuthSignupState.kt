package org.bedu.filmapp.ui.auth_signup

data class AuthSignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = ""
)
