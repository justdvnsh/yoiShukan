package org.thatmobiledevguy.yoiShukan.widgets

import android.content.Context

class HistoryWidgetProvider : BaseWidgetProvider() {
    override fun getWidgetFromId(context: Context, id: Int): BaseWidget {
        val habits = getHabitsFromWidgetId(id)
        return if (habits.size == 1) {
            HistoryWidget(
                context,
                id,
                habits[0]
            )
        } else {
            StackWidget(context, id, StackWidgetType.HISTORY, habits)
        }
    }
}
