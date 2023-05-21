package org.bedu.filmapp.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override fun getUserById(id: String): Flow<Response<User?>> = callbackFlow {
        val snapshotListener = collectionReference.document(id).addSnapshotListener{ snapshot, e ->
            val userResponse = if (snapshot != null) {
                val user = snapshot.toObject(User::class.java)
                Response.Success(user)
            } else {
                Response.Failure((e ?: "error desconocido") as Exception)
            }
            trySend(userResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getUsers(): Flow<Response<List<User>>> = callbackFlow{
        val snapshotListener = collectionReference.addSnapshotListener{ snapshot, e ->
            val usersResponse = if (snapshot != null) {
                val users = snapshot.toObjects(User::class.java)
                Response.Success(users)
            } else {
                Response.Failure((e ?: "error desconocido") as Exception)
            }
            trySend(usersResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}