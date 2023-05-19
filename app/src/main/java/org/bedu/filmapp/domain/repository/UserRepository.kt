package org.bedu.filmapp.domain.repository

import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User

interface UserRepository {
    suspend fun create(user: User): Response<Boolean>
}