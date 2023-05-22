package org.bedu.filmapp.domain.model

data class Post(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var year: Int = 0,
    var duration: String = "",
    var gender: String = "",
    var category: String = "",
    var createdBy: String = "",
    var imagePost: String = "",
    var imagePortedPost: String = "",
    var likes: ArrayList<String> = ArrayList(),
    var favorites: ArrayList<String> = ArrayList(),
    var watch: ArrayList<String> = ArrayList()
)
