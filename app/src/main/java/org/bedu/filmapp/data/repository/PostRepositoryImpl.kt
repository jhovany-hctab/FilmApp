package org.bedu.filmapp.data.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.bedu.filmapp.core.Constants.POSTS
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Named

class PostRepositoryImpl @Inject constructor(
    @Named(POSTS) private val collectionReference: CollectionReference
): PostRepository {
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
}