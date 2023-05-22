package org.bedu.filmapp.domain.use_cases.post

data class PostUseCases(
    val postsGet: PostsGet,
    val postGetData: PostGetData,
    val postLike: PostLike,
    val postLikeDelete: PostLikeDelete,
    val postAddFavorite: PostAddFavorite,
    val postRemoveFavorite: PostRemoveFavorite,
    val postAddWatch: PostAddWatch,
    val postRemoveWatch: PostRemoveWatch,
    val postFavorite: PostFavorite
)