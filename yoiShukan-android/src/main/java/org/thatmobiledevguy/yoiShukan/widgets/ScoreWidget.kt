package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.thatmobiledevguy.platform.gui.toInt
import org.thatmobiledevguy.yoiShukan.activities.common.views.ScoreChart
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.ui.screens.habits.show.views.ScoreCardPresenter
import org.thatmobiledevguy.yoiShukan.core.ui.views.WidgetTheme
import org.thatmobiledevguy.yoiShukan.widgets.views.GraphWidgetView

class ScoreWidget(
    context: Context,
    id: Int,
    private val habit: Habit,
    stacked: Boolean = false
) : BaseWidget(context, id, stacked) {
    override val defaultHeight: Int = 300
    override val defaultWidth: Int = 300

    override fun getOnClickPendingIntent(context: Context): PendingIntent =
        pendingIntentFactory.showHabit(habit)

    override fun refreshData(view: View) {
        val viewModel = ScoreCardPresenter.buildState(
            habit = habit,
            firstWeekday = prefs.firstWeekdayInt,
            spinnerPosition = prefs.scoreCardSpinnerPosition,
            theme = WidgetTheme()
        )
        val widgetView = view as GraphWidgetView
        widgetView.setBackgroundAlpha(preferedBackgroundAlpha)
        if (preferedBackgroundAlpha >= 255) widgetView.setShadowAlpha(0x4f)
        (widgetView.dataView as ScoreChart).apply {
            setIsTransparencyEnabled(true)
            setBucketSize(viewModel.bucketSize)
            setColor(WidgetTheme().color(habit.color).toInt())
            setScores(viewModel.scores)
        }
    }

    override fun buildView() =
        GraphWidgetView(context, ScoreChart(context)).apply {
            setTitle(habit.name)
        }
}
