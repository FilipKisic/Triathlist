package hr.algebra.triathlist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hr.algebra.triathlist.converters.ActivityTypeConverter
import hr.algebra.triathlist.converters.LocalDateTimeConverter
import hr.algebra.triathlist.dao.ActivityDao
import hr.algebra.triathlist.dao.UserDao
import hr.algebra.triathlist.model.Activity
import hr.algebra.triathlist.model.User

@Database(entities = [Activity::class, User::class], version = 1, exportSchema = false)
@TypeConverters(ActivityTypeConverter::class, LocalDateTimeConverter::class)
abstract class TriathlistDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: TriathlistDatabase? = null

        fun getDatabase(context: Context): TriathlistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TriathlistDatabase::class.java,
                    "triathlist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}