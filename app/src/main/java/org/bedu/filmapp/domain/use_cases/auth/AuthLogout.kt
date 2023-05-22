package org.bedu.filmapp.domain.use_cases.auth

import org.bedu.filmapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthLogout @Inject constructor(private val repository: AuthRepository){
    suspend operator fun invoke() = repository.logout()
}