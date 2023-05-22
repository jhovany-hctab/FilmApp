package org.bedu.filmapp.domain.repository

import com.google.firebase.auth.FirebaseUser
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Response<FirebaseUser>
    suspend fun signup(user: User): Response<FirebaseUser>

    suspend fun logout(): Response<Boolean>
}