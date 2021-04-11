package hr.algebra.triathlist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    val email: String,
    val password: String,
    val gender: String?,
    val firstName: String?,
    val lastName: String?,
    var age: Int?,
    var height: Int?,
    var weight: Int?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var idUser: Int = 0
}