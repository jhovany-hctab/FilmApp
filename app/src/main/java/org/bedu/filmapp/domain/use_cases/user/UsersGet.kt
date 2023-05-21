package org.bedu.filmapp.domain.use_cases.user

import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject

class UsersGet @Inject constructor(private val repository: UserRepository) {
    operator fun invoke() = repository.getUsers()
}