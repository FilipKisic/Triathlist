package hr.algebra.triathlist.dal

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.triathlist.model.SwimInfo

private const val DB_NAME = "triathlist.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "triathlist"
private val CREATE_TABLE = "create table $TABLE_NAME ( " +
        "${SwimInfo::_id.name} integer primary key autoincrement, " +
        "${SwimInfo::dayOfWeek.name} text not null, " +
        "${SwimInfo::laps.name} integer not null, " +
        "${SwimInfo::totalTime.name} text not null, " +
        "${SwimInfo::distance.name} integer not null)"

private const val DROP_TABLE = "drop table $TABLE_NAME"

class SqlHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), Repository {
    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL(DROP_TABLE)
        onCreate(database)
    }

    override fun insert(values: ContentValues?): Long {
        return writableDatabase.insert(TABLE_NAME, null, values)
    }

    override fun query(projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? =
        readableDatabase.query(
            TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

    override fun update(values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int =
        writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)

    override fun delete(selection: String?, selectionArgs: Array<String>?): Int =
        writableDatabase.delete(TABLE_NAME, selection, selectionArgs)

}