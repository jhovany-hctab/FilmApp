package org.bedu.filmapp.ui.profile_users

import androidx.lifecycle.SavedStateHandle
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
class ProfileUsersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val postUseCases: PostUseCases
): ViewModel() {

    private val userId = savedStateHandle.get<String>("userId")

    var userDataResponse = MutableStateFlow<Response<User?>?>(null)
    var postPopResponse = MutableStateFlow<Response<List<Post>>?>(null)

    var followResponse = MutableStateFlow<Response<Boolean>?>(null)
    var followDeleteResponse = MutableStateFlow<Response<Boolean>?>(null)

    var postFavoriteResponse = MutableStateFlow<Response<List<Post>>?>(null)

    val user = authUseCases.getCurrentUser()!!.uid

    init {
        getUserById()
    }

    fun follow() = viewModelScope.launch {
        followResponse.value = Response.Loading
        val result = userUseCases.userFollow(user!!, userId!!)
        followResponse.value = result

    }

    fun followDelete() = viewModelScope.launch {
        followDeleteResponse.value = Response.Loading
        val result = userUseCases.userFollowDelete(user!!, userId!!)
        followDeleteResponse.value = result

    }
    private fun getUserById() = viewModelScope.launch {
        userDataResponse.value = Response.Loading
        userUseCases.userById(userId!!)
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

    fun getFavoritePosts(idUser: String? = null) = viewModelScope.launch {
        postFavoriteResponse.value = Response.Loading
        postUseCases.postFavorite(idUser!!)
            .collect() {
                postFavoriteResponse.value = it
            }
    }
}