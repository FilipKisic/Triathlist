package hr.algebra.triathlist.model


data class User(
    val idUser: Int,
    val username: String,
    val password: String,
    val email: String,
    val gender: String?,
    val firstName: String?,
    val lastName: String?,
    var age: Int?,
    var height: Int?,
    var weight: Int?
)