package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Intent
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.models.Timestamp
import org.thatmobiledevguy.yoiShukan.intents.PendingIntentFactory
import java.lang.IllegalStateException

enum class StackWidgetType(val value: Int) {
    CHECKMARK(0), FREQUENCY(1), SCORE(2), // habit strength widget
    HISTORY(3), STREAKS(4), TARGET(5);

    companion object {
        fun getWidgetTypeFromValue(value: Int): StackWidgetType? {
            return when (value) {
                CHECKMARK.value -> CHECKMARK
                FREQUENCY.value -> FREQUENCY
                SCORE.value -> SCORE
                HISTORY.value -> HISTORY
                STREAKS.value -> STREAKS
                TARGET.value -> TARGET
                else -> null
            }
        }

        fun getStackWidgetLayoutId(type: StackWidgetType?): Int {
            return when (type) {
                CHECKMARK -> R.layout.checkmark_stackview_widget
                FREQUENCY -> R.layout.frequency_stackview_widget
                SCORE -> R.layout.score_stackview_widget
                HISTORY -> R.layout.history_stackview_widget
                STREAKS -> R.layout.streak_stackview_widget
                TARGET -> R.layout.target_stackview_widget
                else -> throw IllegalStateException()
            }
        }

        fun getStackWidgetAdapterViewId(type: StackWidgetType?): Int {
            return when (type) {
                CHECKMARK -> R.id.checkmarkStackWidgetView
                FREQUENCY -> R.id.frequencyStackWidgetView
                SCORE -> R.id.scoreStackWidgetView
                HISTORY -> R.id.historyStackWidgetView
                STREAKS -> R.id.streakStackWidgetView
                TARGET -> R.id.targetStackWidgetView
                else -> throw IllegalStateException()
            }
        }

        fun getStackWidgetEmptyViewId(type: StackWidgetType?): Int {
            return when (type) {
                CHECKMARK -> R.id.checkmarkStackWidgetEmptyView
                FREQUENCY -> R.id.frequencyStackWidgetEmptyView
                SCORE -> R.id.scoreStackWidgetEmptyView
                HISTORY -> R.id.historyStackWidgetEmptyView
                STREAKS -> R.id.streakStackWidgetEmptyView
                TARGET -> R.id.targetStackWidgetEmptyView
                else -> throw IllegalStateException()
            }
        }

        fun getPendingIntentTemplate(
            factory: PendingIntentFactory,
            widgetType: StackWidgetType,
            habits: List<Habit>
        ): PendingIntent {
            val containsNumerical = habits.any { it.isNumerical }
            return when (widgetType) {
                CHECKMARK -> if (containsNumerical) {
                    factory.showNumberPickerTemplate()
                } else {
                    factory.toggleCheckmarkTemplate()
                }
                FREQUENCY, SCORE, HISTORY, STREAKS, TARGET -> factory.showHabitTemplate()
            }
        }

        fun getIntentFillIn(
            factory: PendingIntentFactory,
            widgetType: StackWidgetType,
            habit: Habit,
            allHabitsInStackWidget: List<Habit>,
            timestamp: Timestamp
        ): Intent {
            val containsNumerical = allHabitsInStackWidget.any { it.isNumerical }
            return when (widgetType) {
                CHECKMARK -> if (containsNumerical) {
                    factory.showNumberPickerFillIn(habit, timestamp)
                } else {
                    factory.toggleCheckmarkFillIn(habit, timestamp)
                }
                FREQUENCY, SCORE, HISTORY, STREAKS, TARGET -> factory.showHabitFillIn(habit)
            }
        }
    }
}
