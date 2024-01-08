package org.thatmobiledevguy.yoiShukan.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.thatmobiledevguy.yoiShukan.YoiShukanApplication.Companion.isTestMode
import org.thatmobiledevguy.yoiShukan.YoiShukanDatabaseOpener
import org.thatmobiledevguy.yoiShukan.core.DATABASE_FILENAME
import org.thatmobiledevguy.yoiShukan.core.DATABASE_VERSION
import org.thatmobiledevguy.yoiShukan.core.utils.DateFormats.Companion.getBackupDateFormat
import org.thatmobiledevguy.yoiShukan.core.utils.DateUtils.Companion.getLocalTime
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat

object DatabaseUtils {
    private var opener: YoiShukanDatabaseOpener? = null

    @JvmStatic
    fun getDatabaseFile(context: Context): File {
        val databaseFilename = databaseFilename
        val root = context.filesDir.path
        return File("$root/../databases/$databaseFilename")
    }

    private val databaseFilename: String
        get() {
            var databaseFilename: String = DATABASE_FILENAME
            if (isTestMode()) databaseFilename = "test.db"
            return databaseFilename
        }

    fun initializeDatabase(context: Context?) {
        opener = YoiShukanDatabaseOpener(
            context!!,
            databaseFilename,
            DATABASE_VERSION
        )
    }

    @JvmStatic
    @Throws(IOException::class)
    fun saveDatabaseCopy(context: Context, dir: File): String {
        val dateFormat: SimpleDateFormat = getBackupDateFormat()
        val date = dateFormat.format(getLocalTime())
        val filename = "${dir.absolutePath}/YoiShukan Backup $date.db"
        Log.i("DatabaseUtils", "Writing: $filename")
        val db = getDatabaseFile(context)
        val dbCopy = File(filename)
        db.copyTo(dbCopy)
        return dbCopy.absolutePath
    }

    fun openDatabase(): SQLiteDatabase {
        checkNotNull(opener)
        return opener!!.writableDatabase
    }
}
