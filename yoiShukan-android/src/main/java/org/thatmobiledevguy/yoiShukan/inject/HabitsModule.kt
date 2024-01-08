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

package org.thatmobiledevguy.yoiShukan.inject

import dagger.Module
import dagger.Provides
import org.thatmobiledevguy.yoiShukan.core.AppScope
import org.thatmobiledevguy.yoiShukan.core.commands.CommandRunner
import org.thatmobiledevguy.yoiShukan.core.database.Database
import org.thatmobiledevguy.yoiShukan.core.database.DatabaseOpener
import org.thatmobiledevguy.yoiShukan.core.io.Logging
import org.thatmobiledevguy.yoiShukan.core.models.HabitList
import org.thatmobiledevguy.yoiShukan.core.models.ModelFactory
import org.thatmobiledevguy.yoiShukan.core.models.sqlite.SQLModelFactory
import org.thatmobiledevguy.yoiShukan.core.models.sqlite.SQLiteHabitList
import org.thatmobiledevguy.yoiShukan.core.preferences.Preferences
import org.thatmobiledevguy.yoiShukan.core.preferences.WidgetPreferences
import org.thatmobiledevguy.yoiShukan.core.reminders.ReminderScheduler
import org.thatmobiledevguy.yoiShukan.core.tasks.TaskRunner
import org.thatmobiledevguy.yoiShukan.core.ui.NotificationTray
import org.thatmobiledevguy.yoiShukan.database.AndroidDatabase
import org.thatmobiledevguy.yoiShukan.database.AndroidDatabaseOpener
import org.thatmobiledevguy.yoiShukan.intents.IntentScheduler
import org.thatmobiledevguy.yoiShukan.io.AndroidLogging
import org.thatmobiledevguy.yoiShukan.notifications.AndroidNotificationTray
import org.thatmobiledevguy.yoiShukan.preferences.SharedPreferencesStorage
import org.thatmobiledevguy.yoiShukan.utils.DatabaseUtils
import java.io.File

@Module
class HabitsModule(dbFile: File) {

    val db: Database = AndroidDatabase(DatabaseUtils.openDatabase(), dbFile)

    @Provides
    @AppScope
    fun getPreferences(storage: SharedPreferencesStorage): Preferences {
        return Preferences(storage)
    }

    @Provides
    @AppScope
    fun getReminderScheduler(
        sys: IntentScheduler,
        commandRunner: CommandRunner,
        habitList: HabitList,
        widgetPreferences: WidgetPreferences
    ): ReminderScheduler {
        return ReminderScheduler(commandRunner, habitList, sys, widgetPreferences)
    }

    @Provides
    @AppScope
    fun getTray(
        taskRunner: TaskRunner,
        commandRunner: CommandRunner,
        preferences: Preferences,
        screen: AndroidNotificationTray
    ): NotificationTray {
        return NotificationTray(taskRunner, commandRunner, preferences, screen)
    }

    @Provides
    @AppScope
    fun getWidgetPreferences(
        storage: SharedPreferencesStorage
    ): WidgetPreferences {
        return WidgetPreferences(storage)
    }

    @Provides
    @AppScope
    fun getModelFactory(): ModelFactory {
        return SQLModelFactory(db)
    }

    @Provides
    @AppScope
    fun getHabitList(list: SQLiteHabitList): HabitList {
        return list
    }

    @Provides
    @AppScope
    fun getDatabaseOpener(opener: AndroidDatabaseOpener): DatabaseOpener {
        return opener
    }

    @Provides
    @AppScope
    fun getLogging(): Logging {
        return AndroidLogging()
    }

    @Provides
    @AppScope
    fun getDatabase(): Database {
        return db
    }
}
