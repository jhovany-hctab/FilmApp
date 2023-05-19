package org.bedu.filmapp.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import org.bedu.filmapp.core.Constants.USERS
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Named

class UserRepositoryImpl @Inject constructor(
    @Named(USERS) private val collectionReference: CollectionReference
): UserRepository {
    override suspend fun create(user: User): Response<Boolean> {
        return try {
            user.password = "****"
            collectionReference.document(user.id).set(user).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }
}