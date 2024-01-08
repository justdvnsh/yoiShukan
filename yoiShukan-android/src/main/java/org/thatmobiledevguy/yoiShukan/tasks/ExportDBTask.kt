/*
 * Copyright (C) 2016-2021 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.thatmobiledevguy.yoiShukan.tasks

import android.content.Context
import org.thatmobiledevguy.yoiShukan.AndroidDirFinder
import org.thatmobiledevguy.yoiShukan.core.tasks.Task
import org.thatmobiledevguy.yoiShukan.inject.AppContext
import org.thatmobiledevguy.yoiShukan.utils.DatabaseUtils.saveDatabaseCopy
import java.io.IOException

class ExportDBTask(
    @param:AppContext private val context: Context,
    private val system: AndroidDirFinder,
    private val listener: Listener
) : Task {
    private var filename: String? = null
    override fun doInBackground() {
        filename = null
        filename = try {
            val dir = system.getFilesDir("Backups") ?: return
            saveDatabaseCopy(context, dir)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun onPostExecute() {
        listener.onExportDBFinished(filename)
    }

    fun interface Listener {
        fun onExportDBFinished(filename: String?)
    }
}
