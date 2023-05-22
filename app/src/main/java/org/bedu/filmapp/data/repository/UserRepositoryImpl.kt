package org.bedu.filmapp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
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

    override fun getUsers(idUser: String): Flow<Response<List<User>>> = callbackFlow{
        val snapshotListener = collectionReference.whereNotEqualTo("id",idUser).addSnapshotListener{ snapshot, e ->
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

    override suspend fun follow(idUser: String, idUserFollowing: String): Response<Boolean> {
        return try {
            collectionReference.document(idUserFollowing).update("follow", FieldValue.arrayUnion(idUser)).await()
            Response.Success(true)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun followDelete(idUser: String, idUserFollowing: String): Response<Boolean> {
        return try {
            collectionReference.document(idUserFollowing).update("follow", FieldValue.arrayRemove(idUser)).await()
            Response.Success(true)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }
}