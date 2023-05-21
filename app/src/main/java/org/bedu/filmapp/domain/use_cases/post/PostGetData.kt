package org.bedu.filmapp.domain.use_cases.post

import org.bedu.filmapp.domain.repository.PostRepository
import javax.inject.Inject

class PostGetData @Inject constructor(private val repository: PostRepository) {
    operator fun invoke(id: String) = repository.getPostData(id)
}