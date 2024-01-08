package org.thatmobiledevguy.yoiShukan

import android.app.backup.BackupAgentHelper
import android.app.backup.FileBackupHelper
import android.app.backup.SharedPreferencesBackupHelper

class YoiShukanBackupAgent : BackupAgentHelper() {
    override fun onCreate() {
        addHelper("preferences", SharedPreferencesBackupHelper(this, "preferences"))
        addHelper("database", FileBackupHelper(this, "../databases/yoiShukan.db"))
    }
}
