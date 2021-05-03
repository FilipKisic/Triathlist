package hr.algebra.triathlist.dao

import androidx.room.*
import hr.algebra.triathlist.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAllUsers(): MutableList<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun authenticateUser(email: String, password: String): User?

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteById(id: Int)
}