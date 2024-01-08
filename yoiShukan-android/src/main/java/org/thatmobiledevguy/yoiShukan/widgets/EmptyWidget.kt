package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.content.Context
import android.view.View
import org.thatmobiledevguy.yoiShukan.widgets.views.EmptyWidgetView

class EmptyWidget(
    context: Context,
    widgetId: Int,
    stacked: Boolean = false
) : BaseWidget(context, widgetId, stacked) {
    override val defaultHeight: Int = 200
    override val defaultWidth: Int = 200

    override fun getOnClickPendingIntent(context: Context): PendingIntent? = null
    override fun refreshData(v: View) {}
    override fun buildView() = EmptyWidgetView(context)
}
