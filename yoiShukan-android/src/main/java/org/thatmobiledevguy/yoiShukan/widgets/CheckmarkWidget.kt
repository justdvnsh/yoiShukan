package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.thatmobiledevguy.platform.gui.toInt
import org.thatmobiledevguy.yoiShukan.core.models.Entry
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.ui.views.WidgetTheme
import org.thatmobiledevguy.yoiShukan.core.utils.DateUtils
import org.thatmobiledevguy.yoiShukan.widgets.views.CheckmarkWidgetView

open class CheckmarkWidget(
    context: Context,
    widgetId: Int,
    protected val habit: Habit,
    stacked: Boolean = false
) : BaseWidget(context, widgetId, stacked) {

    override val defaultHeight: Int = 125
    override val defaultWidth: Int = 125

    override fun getOnClickPendingIntent(context: Context): PendingIntent? {
        return if (habit.isNumerical) {
            pendingIntentFactory.showNumberPicker(habit, DateUtils.getTodayWithOffset())
        } else {
            pendingIntentFactory.toggleCheckmark(habit, null)
        }
    }

    override fun refreshData(widgetView: View) {
        (widgetView as CheckmarkWidgetView).apply {
            val today = DateUtils.getTodayWithOffset()
            setBackgroundAlpha(preferedBackgroundAlpha)
            activeColor = WidgetTheme().color(habit.color).toInt()
            name = habit.name
            entryValue = habit.computedEntries.get(today).value
            if (habit.isNumerical) {
                isNumerical = true
                entryState = getNumericalEntryState()
            } else {
                entryState = habit.computedEntries.get(today).value
            }
            percentage = habit.scores[today].value.toFloat()
            refresh()
        }
    }

    override fun buildView(): View {
        return CheckmarkWidgetView(context)
    }

    private fun getNumericalEntryState(): Int {
        return if (habit.isCompletedToday()) {
            Entry.YES_MANUAL
        } else {
            Entry.NO
        }
    }
}
