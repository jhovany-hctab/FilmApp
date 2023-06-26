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
import org.bedu.filmapp.domain.model.WeatherResponse
import org.bedu.filmapp.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Named
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

    override suspend fun weather(lat: Double, lon: Double): WeatherResponse {
        val client = OkHttpClient()
        val json = Json { ignoreUnknownKeys = true }
        val apiKey = "b78f6b532a7e9f1d6776330a94931cad"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&lang=es"
        val request = Request.Builder().url(url).build()

        return suspendCoroutine { continuation ->
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {
                    val responseBody = response.body()?.string()
                    val weatherResponse = json.decodeFromString<WeatherResponse>(responseBody!!)
                    continuation.resume(weatherResponse)
                }
            })
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