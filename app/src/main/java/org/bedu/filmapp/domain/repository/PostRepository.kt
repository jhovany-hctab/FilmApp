package org.bedu.filmapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response

interface PostRepository {
    fun getFavoritePosts(idUser: String): Flow<Response<List<Post>>>
    fun getPosts(): Flow<Response<List<Post>>>
    fun getPostData(id: String): Flow<Response<Post?>>
    suspend fun like(idPost: String, idUser: String): Response<Boolean>
    suspend fun likeDelete(idPost: String, idUser: String): Response<Boolean>
    suspend fun favorite(idPost: String, idUser: String): Response<Boolean>
    suspend fun favoriteDelete(idPost: String, idUser: String): Response<Boolean>
    suspend fun watch(idPost: String, idUser: String): Response<Boolean>
    suspend fun watchDelete(idPost: String, idUser: String): Response<Boolean>
}