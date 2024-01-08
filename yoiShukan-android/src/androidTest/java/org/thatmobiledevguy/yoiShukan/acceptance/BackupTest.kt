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

package org.thatmobiledevguy.yoiShukan.acceptance

import androidx.test.filters.LargeTest
import org.thatmobiledevguy.yoiShukan.BaseUserInterfaceTest
import org.thatmobiledevguy.yoiShukan.acceptance.steps.CommonSteps.clickText
import org.thatmobiledevguy.yoiShukan.acceptance.steps.CommonSteps.launchApp
import org.thatmobiledevguy.yoiShukan.acceptance.steps.CommonSteps.longClickText
import org.thatmobiledevguy.yoiShukan.acceptance.steps.CommonSteps.verifyDisplaysText
import org.thatmobiledevguy.yoiShukan.acceptance.steps.CommonSteps.verifyDoesNotDisplayText
import org.thatmobiledevguy.yoiShukan.acceptance.steps.ListHabitsSteps.MenuItem.DELETE
import org.thatmobiledevguy.yoiShukan.acceptance.steps.ListHabitsSteps.clickMenu
import org.thatmobiledevguy.yoiShukan.acceptance.steps.clearBackupFolder
import org.thatmobiledevguy.yoiShukan.acceptance.steps.clearDownloadFolder
import org.thatmobiledevguy.yoiShukan.acceptance.steps.copyBackupToDownloadFolder
import org.thatmobiledevguy.yoiShukan.acceptance.steps.exportFullBackup
import org.thatmobiledevguy.yoiShukan.acceptance.steps.importBackupFromDownloadFolder
import org.junit.Test

@LargeTest
class BackupTest : BaseUserInterfaceTest() {
    @Test
    fun shouldExportAndImportBackup() {
        launchApp()
        clearDownloadFolder()
        clearBackupFolder()
        exportFullBackup()
        copyBackupToDownloadFolder()
        longClickText("Wake up early")
        clickMenu(DELETE)
        clickText("Yes")
        verifyDoesNotDisplayText("Wake up early")
        importBackupFromDownloadFolder()
        verifyDisplaysText("Wake up early")
    }
}
