package org.bedu.filmapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import org.bedu.filmapp.domain.use_cases.post.PostUseCases
import org.bedu.filmapp.domain.use_cases.user.UserUseCases
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val postUseCases: PostUseCases
): ViewModel() {

    var userDataResponse = MutableStateFlow<Response<User?>?>(null)
    var postPopResponse = MutableStateFlow<Response<List<Post>>?>(null)

    init {
        getUserById()
    }
    private fun getUserById() = viewModelScope.launch {
        userDataResponse.value = Response.Loading
        userUseCases.userById(authUseCases.getCurrentUser()!!.uid)
            .collect() {
                userDataResponse.value = it
            }
    }
    fun getPosts() = viewModelScope.launch {
        postPopResponse.value = Response.Loading
        postUseCases.postsGet()
            .collect() {
                postPopResponse.value = it
            }
    }
}