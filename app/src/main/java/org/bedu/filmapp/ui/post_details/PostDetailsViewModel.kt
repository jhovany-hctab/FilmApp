package org.bedu.filmapp.ui.post_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.bedu.filmapp.domain.model.Post
import org.bedu.filmapp.domain.model.Response
import org.bedu.filmapp.domain.use_cases.post.PostGetData
import org.bedu.filmapp.domain.use_cases.post.PostUseCases
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val postUseCases: PostUseCases
): ViewModel() {

    private val postId = savedStateHandle.get<String>("postId")
    var postDetailsResponse = MutableStateFlow<Response<Post?>?>(null)

    init {
        postGetData()
    }

    private fun postGetData() = viewModelScope.launch {
        postDetailsResponse.value = Response.Loading
        postUseCases.postGetData(postId!!).collect() {
            postDetailsResponse.value = it
        }
    }
}