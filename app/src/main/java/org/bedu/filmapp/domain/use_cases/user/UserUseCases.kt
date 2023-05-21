package org.bedu.filmapp.domain.use_cases.user

data class UserUseCases(
    val userCreate: UserCreate,
    val userById: UserGetUserById,
    val usersGet: UsersGet
)
