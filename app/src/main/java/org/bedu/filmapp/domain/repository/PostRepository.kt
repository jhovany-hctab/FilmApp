package org.bedu.filmapp.domain.repository

import kotlinx.coroutines.flow.Flow
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response

interface PostRepository {
    fun getPosts(): Flow<Response<List<Post>>>
    fun getPostData(id: String): Flow<Response<Post?>>
}