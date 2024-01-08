package org.thatmobiledevguy.yoiShukan.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.widget.RemoteViews
import org.thatmobiledevguy.yoiShukan.YoiShukanApplication
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.models.HabitList
import org.thatmobiledevguy.yoiShukan.core.models.HabitNotFoundException
import org.thatmobiledevguy.yoiShukan.core.preferences.Preferences
import org.thatmobiledevguy.yoiShukan.core.preferences.WidgetPreferences
import org.thatmobiledevguy.yoiShukan.utils.InterfaceUtils.dpToPixels
import java.util.ArrayList

abstract class BaseWidgetProvider : AppWidgetProvider() {
    private lateinit var habits: HabitList
    lateinit var preferences: Preferences
        private set
    private lateinit var widgetPrefs: WidgetPreferences
    fun getDimensionsFromOptions(
        ctx: Context,
        options: Bundle
    ): WidgetDimensions {
        val maxWidth = dpToPixels(
            ctx,
            options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH).toFloat()
        ).toInt()
        val maxHeight = dpToPixels(
            ctx,
            options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT).toFloat()
        ).toInt()
        val minWidth = dpToPixels(
            ctx,
            options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH).toFloat()
        ).toInt()
        val minHeight = dpToPixels(
            ctx,
            options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT).toFloat()
        ).toInt()
        return WidgetDimensions(minWidth, maxHeight, maxWidth, minHeight)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        manager: AppWidgetManager,
        widgetId: Int,
        options: Bundle
    ) {
        try {
            updateDependencies(context)
            context.setTheme(R.style.WidgetTheme)
            val widget = getWidgetFromId(context, widgetId)
            val dims = getDimensionsFromOptions(context, options)
            widget.setDimensions(dims)
            updateAppWidget(manager, widget)
        } catch (e: RuntimeException) {
            drawErrorWidget(context, manager, widgetId, e)
            e.printStackTrace()
        }
    }

    override fun onDeleted(context: Context?, ids: IntArray?) {
        if (context == null) throw RuntimeException("context is null")
        if (ids == null) throw RuntimeException("ids is null")
        updateDependencies(context)
        for (id in ids) {
            try {
                val widget = getWidgetFromId(context, id)
                widget.delete()
            } catch (e: HabitNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun onUpdate(
        context: Context,
        manager: AppWidgetManager,
        widgetIds: IntArray
    ) {
        updateDependencies(context)
        context.setTheme(R.style.WidgetTheme)
        Thread {
            Looper.prepare()
            for (id in widgetIds) update(context, manager, id)
        }.start()
    }

    protected fun getHabitsFromWidgetId(widgetId: Int): List<Habit> {
        val selectedIds = widgetPrefs.getHabitIdsFromWidgetId(widgetId)
        val selectedHabits = ArrayList<Habit>(selectedIds.size)
        for (id in selectedIds) {
            val h = habits.getById(id) ?: throw HabitNotFoundException()
            selectedHabits.add(h)
        }
        return selectedHabits
    }

    protected abstract fun getWidgetFromId(
        context: Context,
        id: Int
    ): BaseWidget

    private fun drawErrorWidget(
        context: Context,
        manager: AppWidgetManager,
        widgetId: Int,
        e: RuntimeException
    ) {
        val errorView = RemoteViews(context.packageName, R.layout.widget_error)
        if (e is HabitNotFoundException) {
            errorView.setCharSequence(
                R.id.label,
                "setText",
                context.getString(R.string.habit_not_found)
            )
        }
        manager.updateAppWidget(widgetId, errorView)
    }

    private fun update(
        context: Context,
        manager: AppWidgetManager,
        widgetId: Int
    ) {
        try {
            val widget = getWidgetFromId(context, widgetId)
            val options = manager.getAppWidgetOptions(widgetId)
            widget.setDimensions(getDimensionsFromOptions(context, options))
            updateAppWidget(manager, widget)
        } catch (e: RuntimeException) {
            drawErrorWidget(context, manager, widgetId, e)
            e.printStackTrace()
        }
    }

    private fun updateDependencies(context: Context) {
        val app = context.applicationContext as YoiShukanApplication
        habits = app.component.habitList
        preferences = app.component.preferences
        widgetPrefs = app.component.widgetPreferences
    }

    companion object {
        fun updateAppWidget(
            manager: AppWidgetManager,
            widget: BaseWidget
        ) {
            val landscape = widget.landscapeRemoteViews
            val portrait = widget.portraitRemoteViews
            val views = RemoteViews(landscape, portrait)
            manager.updateAppWidget(widget.id, views)
        }
    }
}
