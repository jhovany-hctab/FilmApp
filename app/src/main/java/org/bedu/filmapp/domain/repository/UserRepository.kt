package org.bedu.filmapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User

interface UserRepository {
    suspend fun create(user: User): Response<Boolean>
    fun getUserById(id: String): Flow<Response<User?>>
    fun getUsers(idUser: String): Flow<Response<List<User>>>
    suspend fun follow(idUser: String, idUserFollowing: String): Response<Boolean>
    suspend fun followDelete(idUser: String, idUserFollowing: String): Response<Boolean>
}