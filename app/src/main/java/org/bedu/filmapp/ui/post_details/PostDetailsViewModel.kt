package org.bedu.filmapp.ui.post_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import org.bedu.filmapp.domain.use_cases.post.PostGetData
import org.bedu.filmapp.domain.use_cases.post.PostUseCases
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postUseCases: PostUseCases,
    private val authUseCases: AuthUseCases
): ViewModel() {

    private val postId = savedStateHandle.get<String>("postId")
    var postDetailsResponse = MutableStateFlow<Response<Post?>?>(null)
    var postLikeResponse = MutableStateFlow<Response<Boolean>?>(null)
    var postLikeDeleteResponse = MutableStateFlow<Response<Boolean>?>(null)
    var favoriteResponse = MutableStateFlow<Response<Boolean>?>(null)
    var favoriteDeleteResponse = MutableStateFlow<Response<Boolean>?>(null)
    var watchResponse = MutableStateFlow<Response<Boolean>?>(null)
    var watchDeleteResponse = MutableStateFlow<Response<Boolean>?>(null)

    val user = authUseCases.getCurrentUser()!!.uid

    init {
        postGetData()
    }

    fun like() = viewModelScope.launch {
        postLikeResponse.value = Response.Loading
        val result = postUseCases.postLike(postId!!, user!!)
        postLikeResponse.value = result

    }

    fun likeDelete() = viewModelScope.launch {
        postLikeDeleteResponse.value = Response.Loading
        val result = postUseCases.postLikeDelete(postId!!, user!!)
        postLikeDeleteResponse.value = result

    }
    fun favorite() = viewModelScope.launch {
        favoriteResponse.value = Response.Loading
        val result = postUseCases.postAddFavorite(postId!!, user!!)
        favoriteResponse.value = result

    }

    fun favoriteDelete() = viewModelScope.launch {
        favoriteDeleteResponse.value = Response.Loading
        val result = postUseCases.postRemoveFavorite(postId!!, user!!)
        favoriteDeleteResponse.value = result

    }
    fun watch() = viewModelScope.launch {
        watchResponse.value = Response.Loading
        val result = postUseCases.postAddWatch(postId!!, user!!)
        watchResponse.value = result

    }

    fun watchDelete() = viewModelScope.launch {
        watchDeleteResponse.value = Response.Loading
        val result = postUseCases.postRemoveWatch(postId!!, user!!)
        watchDeleteResponse.value = result

    }
    private fun postGetData() = viewModelScope.launch {
        postDetailsResponse.value = Response.Loading
        postUseCases.postGetData(postId!!).collect() {
            postDetailsResponse.value = it
        }
    }
}