package org.bedu.filmapp.domain.use_cases.user

import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject

class UserGetUserById @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(id: String) = repository.getUserById(id)
}