package org.bedu.filmapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.model.User
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import org.bedu.filmapp.domain.use_cases.post.PostUseCases
import org.bedu.filmapp.domain.use_cases.user.UserUseCases
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val postUseCases: PostUseCases
): ViewModel() {

    var userDataResponse = MutableStateFlow<Response<User?>?>(null)
    var usersResponse = MutableStateFlow<Response<List<User>>?>(null)
    var postPopResponse = MutableStateFlow<Response<List<Post>>?>(null)
    var postFavoriteResponse = MutableStateFlow<Response<List<Post>>?>(null)

    val userCurrent = authUseCases.getCurrentUser()!!.uid

    init {
        getUserById()
    }
    private fun getUserById() = viewModelScope.launch {
        userDataResponse.value = Response.Loading
        userUseCases.userById(userCurrent)
            .collect() {
                userDataResponse.value = it
            }
    }
    fun getUsers() = viewModelScope.launch {
        usersResponse.value = Response.Loading
        userUseCases.usersGet(userCurrent)
            .collect() {
                usersResponse.value = it
            }
    }
    fun getPosts() = viewModelScope.launch {
        postPopResponse.value = Response.Loading
        postUseCases.postsGet()
            .collect() {
                postPopResponse.value = it
            }
    }
    fun getFavoritePosts() = viewModelScope.launch {
        postFavoriteResponse.value = Response.Loading
        postUseCases.postFavorite(userCurrent)
            .collect() {
                postFavoriteResponse.value = it
            }
    }
}