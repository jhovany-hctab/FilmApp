package org.bedu.filmapp.domain.use_cases.post

import org.bedu.filmapp.domain.repository.PostRepository
import javax.inject.Inject

class PostsGet @Inject constructor(private val repository: PostRepository) {
    operator fun invoke() = repository.getPosts()
}