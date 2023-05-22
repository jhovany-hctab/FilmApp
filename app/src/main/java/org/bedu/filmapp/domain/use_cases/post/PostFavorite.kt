package org.bedu.filmapp.domain.use_cases.post

import org.bedu.filmapp.domain.repository.PostRepository
import javax.inject.Inject

class PostFavorite @Inject constructor(private val repository: PostRepository) {
    operator fun invoke(idUser: String) = repository.getFavoritePosts(idUser)
}