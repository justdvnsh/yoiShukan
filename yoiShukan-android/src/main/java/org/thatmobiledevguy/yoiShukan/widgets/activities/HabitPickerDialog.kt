package org.thatmobiledevguy.yoiShukan.widgets.activities

import android.app.Activity
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import org.thatmobiledevguy.yoiShukan.YoiShukanApplication
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.activities.AndroidThemeSwitcher
import org.thatmobiledevguy.yoiShukan.core.preferences.WidgetPreferences
import org.thatmobiledevguy.yoiShukan.widgets.WidgetUpdater

class BooleanHabitPickerDialog : HabitPickerDialog() {
    override fun shouldHideNumerical() = true
    override fun getEmptyMessage() = R.string.no_boolean_habits
}

class NumericalHabitPickerDialog : HabitPickerDialog() {
    override fun shouldHideBoolean() = true
    override fun getEmptyMessage() = R.string.no_numerical_habits
}

open class HabitPickerDialog : Activity() {

    private var widgetId = 0
    private lateinit var widgetPreferences: WidgetPreferences
    private lateinit var widgetUpdater: WidgetUpdater

    protected open fun shouldHideNumerical() = false
    protected open fun shouldHideBoolean() = false
    protected open fun getEmptyMessage() = R.string.no_habits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = (applicationContext as YoiShukanApplication).component
        AndroidThemeSwitcher(this, component.preferences).apply()
        val habitList = component.habitList
        widgetPreferences = component.widgetPreferences
        widgetUpdater = component.widgetUpdater
        widgetId = intent.extras?.getInt(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID) ?: 0

        val habitIds = ArrayList<Long>()
        val habitNames = ArrayList<String>()
        for (h in habitList) {
            if (h.isArchived) continue
            if (h.isNumerical and shouldHideNumerical()) continue
            if (!h.isNumerical and shouldHideBoolean()) continue
            habitIds.add(h.id!!)
            habitNames.add(h.name)
        }

        if (habitNames.isEmpty()) {
            setContentView(R.layout.widget_empty_activity)
            findViewById<TextView>(R.id.message).setText(getEmptyMessage())
            return
        }

        setContentView(R.layout.widget_configure_activity)
        val listView = findViewById<ListView>(R.id.listView)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        with(listView) {
            adapter = ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                habitNames
            )
            setOnItemClickListener { parent, view, position, id ->
                confirm(mutableListOf(habitIds[position]))
            }
        }
    }

    fun confirm(selectedIds: List<Long>) {
        widgetPreferences.addWidget(widgetId, selectedIds.toLongArray())
        widgetUpdater.updateWidgets()
        setResult(
            RESULT_OK,
            Intent().apply {
                putExtra(EXTRA_APPWIDGET_ID, widgetId)
            }
        )
        finish()
    }
}
