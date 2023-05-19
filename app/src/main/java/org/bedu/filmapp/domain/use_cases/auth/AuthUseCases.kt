package org.bedu.filmapp.domain.use_cases.auth

data class AuthUseCases(
    val getCurrentUser: GetCurrentUser,
    val authLogin: AuthLogin,
    val authSignup: AuthSignup
)
