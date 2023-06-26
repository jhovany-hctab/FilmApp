package org.bedu.filmapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bedu.filmapp.core.Constants.POSTS
import org.bedu.filmapp.core.Constants.USERS
import org.bedu.filmapp.data.repository.AuthRepositoryImpl
import org.bedu.filmapp.data.repository.PostRepositoryImpl
import org.bedu.filmapp.data.repository.UserRepositoryImpl
import org.bedu.filmapp.domain.repository.AuthRepository
import org.bedu.filmapp.domain.repository.PostRepository
import org.bedu.filmapp.domain.repository.UserRepository
import org.bedu.filmapp.domain.use_cases.auth.AuthLogin
import org.bedu.filmapp.domain.use_cases.auth.AuthLogout
import org.bedu.filmapp.domain.use_cases.auth.AuthSignup
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import org.bedu.filmapp.domain.use_cases.auth.GetCurrentUser
import org.bedu.filmapp.domain.use_cases.post.PostAddFavorite
import org.bedu.filmapp.domain.use_cases.post.PostAddWatch
import org.bedu.filmapp.domain.use_cases.post.PostFavorite
import org.bedu.filmapp.domain.use_cases.post.PostGetData
import org.bedu.filmapp.domain.use_cases.post.PostLike
import org.bedu.filmapp.domain.use_cases.post.PostLikeDelete
import org.bedu.filmapp.domain.use_cases.post.PostRemoveFavorite
import org.bedu.filmapp.domain.use_cases.post.PostRemoveWatch
import org.bedu.filmapp.domain.use_cases.post.PostUseCases
import org.bedu.filmapp.domain.use_cases.post.PostsGet
import org.bedu.filmapp.domain.use_cases.user.UserCreate
import org.bedu.filmapp.domain.use_cases.user.UserFollow
import org.bedu.filmapp.domain.use_cases.user.UserFollowDelete
import org.bedu.filmapp.domain.use_cases.user.UserGetUserById
import org.bedu.filmapp.domain.use_cases.user.UserUseCases
import org.bedu.filmapp.domain.use_cases.user.UsersGet
import org.bedu.filmapp.domain.use_cases.user.WeatherTime
import org.checkerframework.checker.regex.qual.PolyRegex
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideAuthUseCases(repository: AuthRepository) = AuthUseCases(
        getCurrentUser = GetCurrentUser(repository),
        authLogin = AuthLogin(repository),
        authSignup = AuthSignup(repository),
        logout = AuthLogout(repository)
    )

    @Provides
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    fun provideUserUseCases(repository: UserRepository) = UserUseCases(
        userCreate = UserCreate(repository),
        userById = UserGetUserById(repository),
        usersGet = UsersGet(repository),
        userFollow = UserFollow(repository),
        userFollowDelete = UserFollowDelete(repository),
        weatherTime = WeatherTime(repository)
    )

    @Provides
    fun providePostRepository(impl: PostRepositoryImpl): PostRepository = impl

    @Provides
    fun providePostUseCases(repository: PostRepository) = PostUseCases(
        postsGet = PostsGet(repository),
        postGetData = PostGetData(repository),
        postLike = PostLike(repository),
        postLikeDelete = PostLikeDelete(repository),
        postAddFavorite = PostAddFavorite(repository),
        postRemoveFavorite = PostRemoveFavorite(repository),
        postAddWatch = PostAddWatch(repository),
        postRemoveWatch = PostRemoveWatch(repository),
        postFavorite = PostFavorite(repository)
    )

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Named(USERS)
    fun provideUserRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)
    @Provides
    @Named(POSTS)
    fun providePostRef(db: FirebaseFirestore): CollectionReference = db.collection(POSTS)



}