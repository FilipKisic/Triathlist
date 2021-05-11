package hr.algebra.triathlist.model

data class User(
    val email: String? = null,
    val password: String? = null,
    val gender: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    var age: Int? = null,
    var height: Int? = null,
    var weight: Int? = null
)