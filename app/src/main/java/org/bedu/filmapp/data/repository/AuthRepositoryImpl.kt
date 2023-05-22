package org.bedu.filmapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
    ): AuthRepository {


    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Response<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun signup(user: User): Response<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun logout(): Response<Boolean> {
        return try {
            firebaseAuth.signOut()
            Response.Success(true)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }
}