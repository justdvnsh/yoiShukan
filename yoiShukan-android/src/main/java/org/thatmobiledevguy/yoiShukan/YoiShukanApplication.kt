package org.thatmobiledevguy.yoiShukan

import android.app.Application
import android.content.Context
import org.thatmobiledevguy.yoiShukan.core.database.UnsupportedDatabaseVersionException
import org.thatmobiledevguy.yoiShukan.core.reminders.ReminderScheduler
import org.thatmobiledevguy.yoiShukan.core.ui.NotificationTray
import org.thatmobiledevguy.yoiShukan.core.utils.DateUtils.Companion.setStartDayOffset
import org.thatmobiledevguy.yoiShukan.inject.AppContextModule
import org.thatmobiledevguy.yoiShukan.inject.DaggerHabitsApplicationComponent
import org.thatmobiledevguy.yoiShukan.inject.HabitsApplicationComponent
import org.thatmobiledevguy.yoiShukan.inject.HabitsModule
import org.thatmobiledevguy.yoiShukan.utils.DatabaseUtils
import org.thatmobiledevguy.yoiShukan.widgets.WidgetUpdater
import java.io.File

class YoiShukanApplication : Application() {

    private lateinit var context: Context
    private lateinit var widgetUpdater: WidgetUpdater
    private lateinit var reminderScheduler: ReminderScheduler
    private lateinit var notificationTray: NotificationTray

    override fun onCreate() {
        super.onCreate()
        context = this

        if (isTestMode()) {
            val db = DatabaseUtils.getDatabaseFile(context)
            if (db.exists()) db.delete()
        }

        try {
            DatabaseUtils.initializeDatabase(context)
        } catch (e: UnsupportedDatabaseVersionException) {
            val db = DatabaseUtils.getDatabaseFile(context)
            db.renameTo(File(db.absolutePath + ".invalid"))
            DatabaseUtils.initializeDatabase(context)
        }

        val db = DatabaseUtils.getDatabaseFile(this)
        YoiShukanApplication.component = DaggerHabitsApplicationComponent
            .builder()
            .appContextModule(AppContextModule(context))
            .habitsModule(HabitsModule(db))
            .build()

        val prefs = component.preferences
        prefs.lastAppVersion = BuildConfig.VERSION_CODE

        if (prefs.isMidnightDelayEnabled) {
            setStartDayOffset(3, 0)
        } else {
            setStartDayOffset(0, 0)
        }

        val habitList = component.habitList
        for (h in habitList) h.recompute()

        widgetUpdater = component.widgetUpdater.apply {
            startListening()
            scheduleStartDayWidgetUpdate()
        }

        reminderScheduler = component.reminderScheduler
        reminderScheduler.startListening()

        notificationTray = component.notificationTray
        notificationTray.startListening()

        val taskRunner = component.taskRunner
        taskRunner.execute {
            reminderScheduler.scheduleAll()
            widgetUpdater.updateWidgets()
        }
    }

    override fun onTerminate() {
        reminderScheduler.stopListening()
        widgetUpdater.stopListening()
        notificationTray.stopListening()
        super.onTerminate()
    }

    val component: HabitsApplicationComponent
        get() = YoiShukanApplication.component

    companion object {
        lateinit var component: HabitsApplicationComponent

        fun isTestMode(): Boolean {
            return try {
                true
            } catch (e: ClassNotFoundException) {
                false
            }
        }
    }
}
