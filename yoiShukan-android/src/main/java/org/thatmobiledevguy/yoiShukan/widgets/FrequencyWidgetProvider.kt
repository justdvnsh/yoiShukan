package org.thatmobiledevguy.yoiShukan.widgets

import android.content.Context

class FrequencyWidgetProvider : BaseWidgetProvider() {
    override fun getWidgetFromId(context: Context, id: Int): BaseWidget {
        val habits = getHabitsFromWidgetId(id)
        return if (habits.size == 1) {
            FrequencyWidget(
                context,
                id,
                habits[0],
                preferences.firstWeekdayInt
            )
        } else {
            StackWidget(context, id, StackWidgetType.FREQUENCY, habits)
        }
    }
}
