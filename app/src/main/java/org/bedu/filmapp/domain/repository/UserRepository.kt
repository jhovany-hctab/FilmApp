package org.bedu.filmapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User

interface UserRepository {
    suspend fun create(user: User): Response<Boolean>
    fun getUserById(id: String): Flow<Response<User?>>
    fun getUsers(): Flow<Response<List<User>>>
}