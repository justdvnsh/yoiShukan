package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.thatmobiledevguy.platform.gui.AndroidDataView
import org.thatmobiledevguy.platform.time.JavaLocalDateFormatter
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.ui.screens.habits.show.views.HistoryCardPresenter
import org.thatmobiledevguy.yoiShukan.core.ui.views.HistoryChart
import org.thatmobiledevguy.yoiShukan.core.ui.views.WidgetTheme
import org.thatmobiledevguy.yoiShukan.core.utils.DateUtils
import org.thatmobiledevguy.yoiShukan.widgets.views.GraphWidgetView
import java.util.Locale

class HistoryWidget(
    context: Context,
    id: Int,
    private val habit: Habit,
    stacked: Boolean = false
) : BaseWidget(context, id, stacked) {

    override val defaultHeight: Int = 250
    override val defaultWidth: Int = 250

    override fun getOnClickPendingIntent(context: Context): PendingIntent {
        return pendingIntentFactory.showHabit(habit)
    }

    override fun refreshData(view: View) {
        val widgetView = view as GraphWidgetView
        widgetView.setBackgroundAlpha(preferedBackgroundAlpha)
        if (preferedBackgroundAlpha >= 255) widgetView.setShadowAlpha(0x4f)
        val model = HistoryCardPresenter.buildState(
            habit = habit,
            firstWeekday = prefs.firstWeekday,
            theme = WidgetTheme()
        )
        (widgetView.dataView as AndroidDataView).apply {
            val historyChart = (this.view as HistoryChart)
            historyChart.series = model.series
            historyChart.defaultSquare = model.defaultSquare
            historyChart.notesIndicators = model.notesIndicators
        }
    }

    override fun buildView() =
        GraphWidgetView(
            context,
            AndroidDataView(context).apply {
                view = HistoryChart(
                    today = DateUtils.getTodayWithOffset().toLocalDate(),
                    paletteColor = habit.color,
                    theme = WidgetTheme(),
                    dateFormatter = JavaLocalDateFormatter(Locale.getDefault()),
                    firstWeekday = prefs.firstWeekday,
                    series = listOf(),
                    defaultSquare = HistoryChart.Square.OFF,
                    notesIndicators = listOf()
                )
            }
        ).apply {
            setTitle(habit.name)
        }
}
