package org.thatmobiledevguy.yoiShukan

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.thatmobiledevguy.yoiShukan.core.database.MigrationHelper
import org.thatmobiledevguy.yoiShukan.core.database.UnsupportedDatabaseVersionException
import org.thatmobiledevguy.yoiShukan.database.AndroidDatabase
import java.io.File

class YoiShukanDatabaseOpener(
    context: Context,
    private val databaseFilename: String,
    private val version: Int
) : SQLiteOpenHelper(context, databaseFilename, null, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.disableWriteAheadLogging()
        db.version = 8
        onUpgrade(db, -1, version)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.disableWriteAheadLogging()
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.disableWriteAheadLogging()
        if (db.version < 8) throw UnsupportedDatabaseVersionException()
        val helper = MigrationHelper(AndroidDatabase(db, File(databaseFilename)))
        helper.migrateTo(newVersion)
    }

    override fun onDowngrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        throw UnsupportedDatabaseVersionException()
    }
}
