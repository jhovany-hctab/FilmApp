package org.bedu.filmapp.domain.use_cases.user

import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject

class UserCreate @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) = repository.create(user)
}