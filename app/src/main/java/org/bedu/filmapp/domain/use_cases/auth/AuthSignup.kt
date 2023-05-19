package org.bedu.filmapp.domain.use_cases.auth

import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthSignup @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(user: User) = repository.signup(user)
}