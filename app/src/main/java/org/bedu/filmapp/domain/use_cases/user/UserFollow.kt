package org.bedu.filmapp.domain.use_cases.user

import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject

class UserFollow @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(idUser: String, idUserFollowing: String) = repository.follow(idUser, idUserFollowing)
}