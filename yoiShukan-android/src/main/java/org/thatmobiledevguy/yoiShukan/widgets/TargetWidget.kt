package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import kotlinx.coroutines.runBlocking
import org.thatmobiledevguy.platform.gui.toInt
import org.thatmobiledevguy.yoiShukan.activities.common.views.TargetChart
import org.thatmobiledevguy.yoiShukan.activities.habits.show.views.TargetCardView.Companion.intervalToLabel
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.ui.screens.habits.show.views.TargetCardPresenter
import org.thatmobiledevguy.yoiShukan.core.ui.views.WidgetTheme
import org.thatmobiledevguy.yoiShukan.widgets.views.GraphWidgetView

class TargetWidget(
    context: Context,
    id: Int,
    private val habit: Habit,
    stacked: Boolean = false
) : BaseWidget(context, id, stacked) {
    override val defaultHeight: Int = 200
    override val defaultWidth: Int = 200

    override fun getOnClickPendingIntent(context: Context): PendingIntent =
        pendingIntentFactory.showHabit(habit)

    override fun refreshData(view: View) = runBlocking {
        val widgetView = view as GraphWidgetView
        widgetView.setBackgroundAlpha(preferedBackgroundAlpha)
        if (preferedBackgroundAlpha >= 255) widgetView.setShadowAlpha(0x4f)
        val chart = (widgetView.dataView as TargetChart)
        val data = TargetCardPresenter.buildState(
            habit = habit,
            firstWeekday = prefs.firstWeekdayInt,
            theme = WidgetTheme()
        )
        chart.setColor(WidgetTheme().color(habit.color).toInt())
        chart.setTargets(data.targets)
        chart.setLabels(data.intervals.map { intervalToLabel(context.resources, it) })
        chart.setValues(data.values)
    }

    override fun buildView(): View {
        return GraphWidgetView(context, TargetChart(context)).apply {
            setTitle(habit.name)
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    }
}
