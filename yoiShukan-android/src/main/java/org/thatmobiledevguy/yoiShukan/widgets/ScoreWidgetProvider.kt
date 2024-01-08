package org.thatmobiledevguy.yoiShukan.widgets

import android.content.Context

class ScoreWidgetProvider : BaseWidgetProvider() {
    override fun getWidgetFromId(context: Context, id: Int): BaseWidget {
        val habits = getHabitsFromWidgetId(id)
        return if (habits.size == 1) {
            ScoreWidget(context, id, habits[0])
        } else {
            StackWidget(context, id, StackWidgetType.SCORE, habits)
        }
    }
}
