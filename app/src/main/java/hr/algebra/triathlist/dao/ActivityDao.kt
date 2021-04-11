package hr.algebra.triathlist.dao

import androidx.room.*
import hr.algebra.triathlist.model.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg activity: Activity)

    @Query("SELECT * FROM activity")
    fun getAllActivities(): MutableList<Activity>

    @Delete
    suspend fun delete(activity: Activity)

    @Query("DELETE FROM activity WHERE id = :id")
    suspend fun deleteById(id: Int)
}