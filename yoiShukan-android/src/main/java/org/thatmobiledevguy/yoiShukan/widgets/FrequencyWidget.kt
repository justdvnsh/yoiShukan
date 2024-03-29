package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.thatmobiledevguy.platform.gui.toInt
import org.thatmobiledevguy.yoiShukan.activities.common.views.FrequencyChart
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.ui.views.WidgetTheme
import org.thatmobiledevguy.yoiShukan.widgets.views.GraphWidgetView

class FrequencyWidget(
    context: Context,
    widgetId: Int,
    private val habit: Habit,
    private val firstWeekday: Int,
    stacked: Boolean = false
) : BaseWidget(context, widgetId, stacked) {
    override val defaultHeight: Int = 200
    override val defaultWidth: Int = 200

    override fun getOnClickPendingIntent(context: Context): PendingIntent =
        pendingIntentFactory.showHabit(habit)

    override fun refreshData(v: View) {
        val widgetView = v as GraphWidgetView
        widgetView.setTitle(habit.name)
        widgetView.setBackgroundAlpha(preferedBackgroundAlpha)
        if (preferedBackgroundAlpha >= 255) widgetView.setShadowAlpha(0x4f)
        (widgetView.dataView as FrequencyChart).apply {
            setFirstWeekday(firstWeekday)
            setColor(WidgetTheme().color(habit.color).toInt())
            setIsNumerical(habit.isNumerical)
            setFrequency(habit.originalEntries.computeWeekdayFrequency(habit.isNumerical))
        }
    }

    override fun buildView() =
        GraphWidgetView(context, FrequencyChart(context))
}
