package org.thatmobiledevguy.yoiShukan.widgets

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import org.thatmobiledevguy.yoiShukan.core.commands.Command
import org.thatmobiledevguy.yoiShukan.core.commands.CommandRunner
import org.thatmobiledevguy.yoiShukan.core.commands.CreateRepetitionCommand
import org.thatmobiledevguy.yoiShukan.core.preferences.WidgetPreferences
import org.thatmobiledevguy.yoiShukan.core.tasks.TaskRunner
import org.thatmobiledevguy.yoiShukan.core.utils.DateUtils
import org.thatmobiledevguy.yoiShukan.inject.AppContext
import org.thatmobiledevguy.yoiShukan.intents.IntentScheduler
import javax.inject.Inject

/**
 * A WidgetUpdater listens to the commands being executed by the application and
 * updates the home-screen widgets accordingly.
 */
class WidgetUpdater
@Inject constructor(
    @AppContext private val context: Context,
    private val commandRunner: CommandRunner,
    private val taskRunner: TaskRunner,
    private val widgetPrefs: WidgetPreferences,
    private val intentScheduler: IntentScheduler
) : CommandRunner.Listener {

    override fun onCommandFinished(command: Command) {
        if (command is CreateRepetitionCommand) {
            updateWidgets(command.habit.id)
        } else {
            updateWidgets()
        }
    }

    /**
     * Instructs the updater to start listening to commands. If any relevant
     * commands are executed after this method is called, the corresponding
     * widgets will get updated.
     */
    fun startListening() {
        commandRunner.addListener(this)
    }

    /**
     * Instructs the updater to stop listening to commands. Every command
     * executed after this method is called will be ignored by the updater.
     */
    fun stopListening() {
        commandRunner.removeListener(this)
    }

    fun scheduleStartDayWidgetUpdate() {
        val timestamp = DateUtils.getStartOfTomorrowWithOffset()
        intentScheduler.scheduleWidgetUpdate(timestamp)
    }

    fun updateWidgets(modifiedHabitId: Long?) {
        taskRunner.execute {
            updateWidgets(modifiedHabitId, CheckmarkWidgetProvider::class.java)
            updateWidgets(modifiedHabitId, HistoryWidgetProvider::class.java)
            updateWidgets(modifiedHabitId, ScoreWidgetProvider::class.java)
            updateWidgets(modifiedHabitId, StreakWidgetProvider::class.java)
            updateWidgets(modifiedHabitId, FrequencyWidgetProvider::class.java)
            updateWidgets(modifiedHabitId, TargetWidgetProvider::class.java)
        }
    }

    private fun updateWidgets(modifiedHabitId: Long?, providerClass: Class<*>) {
        val widgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
            ComponentName(context, providerClass)
        )

        val modifiedWidgetIds = when (modifiedHabitId) {
            null -> widgetIds.toList()
            else -> widgetIds.filter { w ->
                widgetPrefs.getHabitIdsFromWidgetId(w).contains(modifiedHabitId)
            }
        }

        context.sendBroadcast(
            Intent(context, providerClass).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, modifiedWidgetIds.toIntArray())
            }
        )
    }

    fun updateWidgets() {
        updateWidgets(null)
    }
}
