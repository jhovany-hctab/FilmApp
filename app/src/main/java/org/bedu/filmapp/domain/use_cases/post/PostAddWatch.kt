package org.bedu.filmapp.domain.use_cases.post

import org.bedu.filmapp.domain.repository.PostRepository
import javax.inject.Inject

class PostAddWatch @Inject constructor(private val repository: PostRepository) {
    suspend operator fun invoke(idPost: String, idUser: String) = repository.watch(idPost, idUser)
}