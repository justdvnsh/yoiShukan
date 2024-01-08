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

import android.content.Context
import dagger.Component
import org.thatmobiledevguy.yoiShukan.core.AppScope
import org.thatmobiledevguy.yoiShukan.core.commands.CommandRunner
import org.thatmobiledevguy.yoiShukan.core.io.GenericImporter
import org.thatmobiledevguy.yoiShukan.core.io.Logging
import org.thatmobiledevguy.yoiShukan.core.models.HabitList
import org.thatmobiledevguy.yoiShukan.core.models.ModelFactory
import org.thatmobiledevguy.yoiShukan.core.preferences.Preferences
import org.thatmobiledevguy.yoiShukan.core.preferences.WidgetPreferences
import org.thatmobiledevguy.yoiShukan.core.reminders.ReminderScheduler
import org.thatmobiledevguy.yoiShukan.core.tasks.TaskRunner
import org.thatmobiledevguy.yoiShukan.core.ui.NotificationTray
import org.thatmobiledevguy.yoiShukan.core.ui.screens.habits.list.HabitCardListCache
import org.thatmobiledevguy.yoiShukan.core.utils.MidnightTimer
import org.thatmobiledevguy.yoiShukan.intents.IntentFactory
import org.thatmobiledevguy.yoiShukan.intents.IntentParser
import org.thatmobiledevguy.yoiShukan.intents.PendingIntentFactory
import org.thatmobiledevguy.yoiShukan.receivers.ReminderController
import org.thatmobiledevguy.yoiShukan.tasks.AndroidTaskRunner
import org.thatmobiledevguy.yoiShukan.widgets.WidgetUpdater

@AppScope
@Component(modules = [AppContextModule::class, HabitsModule::class, AndroidTaskRunner::class])
interface HabitsApplicationComponent {
    val commandRunner: CommandRunner

    @get:AppContext
    val context: Context
    val genericImporter: GenericImporter
    val habitCardListCache: HabitCardListCache
    val habitList: HabitList
    val intentFactory: IntentFactory
    val intentParser: IntentParser
    val logging: Logging
    val midnightTimer: MidnightTimer
    val modelFactory: ModelFactory
    val notificationTray: NotificationTray
    val pendingIntentFactory: PendingIntentFactory
    val preferences: Preferences
    val reminderScheduler: ReminderScheduler
    val reminderController: ReminderController
    val taskRunner: TaskRunner
    val widgetPreferences: WidgetPreferences
    val widgetUpdater: WidgetUpdater
}
