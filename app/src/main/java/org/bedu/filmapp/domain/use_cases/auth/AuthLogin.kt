package org.bedu.filmapp.domain.use_cases.auth

import org.bedu.filmapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthLogin @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String) = repository.login(email, password)
}