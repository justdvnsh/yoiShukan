package org.thatmobiledevguy.yoiShukan.widgets

import android.content.Context

class CheckmarkWidgetProvider : BaseWidgetProvider() {
    override fun getWidgetFromId(context: Context, id: Int): BaseWidget {
        val habits = getHabitsFromWidgetId(id)
        return if (habits.size == 1) {
            CheckmarkWidget(context, id, habits[0])
        } else {
            StackWidget(context, id, StackWidgetType.CHECKMARK, habits)
        }
    }
}
