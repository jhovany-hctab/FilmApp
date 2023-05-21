package org.bedu.filmapp.domain.model

data class User(
    var id: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var description: String = "",
    var imageProfile: String = "",
    var imagePortedProfile: String = ""
)
