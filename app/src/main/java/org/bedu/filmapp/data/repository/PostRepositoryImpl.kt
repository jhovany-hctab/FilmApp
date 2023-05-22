package org.bedu.filmapp.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.bedu.filmapp.core.Constants.POSTS
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.repository.PostRepository
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

class PostRepositoryImpl @Inject constructor(
    @Named(POSTS) private val collectionReference: CollectionReference
): PostRepository {
    override fun getFavoritePosts(idUser: String): Flow<Response<List<Post>>> = callbackFlow{
        val snapshotListener = collectionReference
            .whereArrayContains("favorites", idUser)
            .addSnapshotListener{ snapshot, e ->
            val postResponse = if (snapshot != null) {
                val post = snapshot.toObjects(Post::class.java)
                Response.Success(post)
            } else {
                Response.Failure(e!!)
            }
            trySend(postResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getPosts(): Flow<Response<List<Post>>> = callbackFlow{
        val snapshotListener = collectionReference.addSnapshotListener{ snapshot, e ->
            val postResponse = if (snapshot != null) {
                val post = snapshot.toObjects(Post::class.java)
                Response.Success(post)
            } else {
                Response.Failure(e!!)
            }
            trySend(postResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getPostData(id: String): Flow<Response<Post?>> = callbackFlow {
        val snapshotListener = collectionReference.document(id).addSnapshotListener{ snapshot, e ->
            val postResponse = if (snapshot != null) {
                val post = snapshot.toObject(Post::class.java)
                Response.Success(post)
            } else {
                Response.Failure(e!!)
            }
            trySend(postResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun like(idPost: String, idUser: String): Response<Boolean> {
        return try {
            collectionReference.document(idPost).update("likes", FieldValue.arrayUnion(idUser)).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun likeDelete(idPost: String, idUser: String): Response<Boolean> {
        return try {
            collectionReference.document(idPost).update("likes", FieldValue.arrayRemove(idUser)).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun favorite(idPost: String, idUser: String): Response<Boolean> {
        return try {
            collectionReference.document(idPost).update("favorites", FieldValue.arrayUnion(idUser)).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun favoriteDelete(idPost: String, idUser: String): Response<Boolean> {
        return try {
            collectionReference.document(idPost).update("favorites", FieldValue.arrayRemove(idUser)).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun watch(idPost: String, idUser: String): Response<Boolean> {
        return try {
            collectionReference.document(idPost).update("watch", FieldValue.arrayUnion(idUser)).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }

    override suspend fun watchDelete(idPost: String, idUser: String): Response<Boolean> {
        return try {
            collectionReference.document(idPost).update("watch", FieldValue.arrayRemove(idUser)).await()
            Response.Success(true)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Failure(e)
        }
    }
}