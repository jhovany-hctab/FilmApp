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
import org.bedu.filmapp.core.Constants.USERS
import org.bedu.filmapp.data.repository.AuthRepositoryImpl
import org.bedu.filmapp.data.repository.UserRepositoryImpl
import org.bedu.filmapp.domain.repository.AuthRepository
import org.bedu.filmapp.domain.repository.UserRepository
import org.bedu.filmapp.domain.use_cases.auth.AuthLogin
import org.bedu.filmapp.domain.use_cases.auth.AuthSignup
import org.bedu.filmapp.domain.use_cases.auth.AuthUseCases
import org.bedu.filmapp.domain.use_cases.auth.GetCurrentUser
import org.bedu.filmapp.domain.use_cases.user.UserCreate
import org.bedu.filmapp.domain.use_cases.user.UserUseCases
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
        authSignup = AuthSignup(repository)
    )

    @Provides
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    fun provideUserUseCases(repository: UserRepository) = UserUseCases(
        userCreate = UserCreate(repository)
    )

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Named(USERS)
    fun provideUserRef(db: FirebaseFirestore): CollectionReference = db.collection(USERS)



}