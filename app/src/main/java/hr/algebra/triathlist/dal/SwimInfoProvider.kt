package hr.algebra.triathlist.dal

import android.content.*
import android.database.Cursor
import android.net.Uri
import hr.algebra.triathlist.factory.getRepository
import hr.algebra.triathlist.model.SwimInfo
import java.lang.IllegalArgumentException

private const val AUTHORITY = "hr.algebra.triathlist.dal.provider"
private const val PATH = "triathlist"
val TRIATHLIST_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val SWIM_INFOS = 10   //??
private const val SWIM_INFO_ID = 20 //??
private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, SWIM_INFOS)
    addURI(AUTHORITY, "$PATH/#", SWIM_INFO_ID)
    this
}
private const val CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH
private const val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH

class SwimInfoProvider : ContentProvider() {
    private lateinit var repository: Repository

    override fun onCreate(): Boolean {
        repository = getRepository(context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = repository.insert(values)
        return ContentUris.withAppendedId(TRIATHLIST_PROVIDER_CONTENT_URI, id)
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = repository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            SWIM_INFOS -> return repository.update(values, selection, selectionArgs)
            SWIM_INFO_ID -> {
                val id = uri.lastPathSegment
                if (id != null)
                    return repository.update(values, "${SwimInfo::_id.name} = ?", arrayOf(id))
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when (URI_MATCHER.match(uri)) {
            SWIM_INFOS -> return repository.delete(selection, selectionArgs)
            SWIM_INFO_ID -> {
                val id = uri.lastPathSegment
                if (id != null)
                    return repository.delete("${SwimInfo::_id.name} = ?", arrayOf(id))
            }
        }
        throw IllegalArgumentException("Wrong URI")
    }

    override fun getType(uri: Uri): String? {
        when (URI_MATCHER.match(uri)) {
            SWIM_INFOS -> return CONTENT_DIR_TYPE
            SWIM_INFO_ID -> return CONTENT_ITEM_TYPE
        }
        throw IllegalArgumentException("Wrong URI")
    }
}