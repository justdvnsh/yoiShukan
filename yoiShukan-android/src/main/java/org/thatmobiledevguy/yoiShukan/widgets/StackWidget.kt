package org.thatmobiledevguy.yoiShukan.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import org.thatmobiledevguy.platform.utils.StringUtils
import org.thatmobiledevguy.yoiShukan.core.models.Habit

class StackWidget(
    context: Context,
    widgetId: Int,
    private val widgetType: StackWidgetType,
    private val habits: List<Habit>,
    stacked: Boolean = true
) : BaseWidget(context, widgetId, stacked) {
    override val defaultHeight: Int = 0
    override val defaultWidth: Int = 0

    override fun getOnClickPendingIntent(context: Context): PendingIntent? = null

    override fun refreshData(v: View) {
        // unused
    }

    override fun buildView(): View? {
        // unused
        return null
    }

    override fun getRemoteViews(width: Int, height: Int): RemoteViews {
        val manager = AppWidgetManager.getInstance(context)
        val remoteViews =
            RemoteViews(context.packageName, StackWidgetType.getStackWidgetLayoutId(widgetType))
        val serviceIntent = Intent(context, StackWidgetService::class.java)
        val habitIds = StringUtils.joinLongs(habits.map { it.id!! }.toLongArray())

        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        serviceIntent.putExtra(StackWidgetService.WIDGET_TYPE, widgetType.value)
        serviceIntent.putExtra(StackWidgetService.HABIT_IDS, habitIds)
        serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))
        remoteViews.setRemoteAdapter(
            StackWidgetType.getStackWidgetAdapterViewId(widgetType),
            serviceIntent
        )
        manager.notifyAppWidgetViewDataChanged(
            id,
            StackWidgetType.getStackWidgetAdapterViewId(widgetType)
        )
        remoteViews.setEmptyView(
            StackWidgetType.getStackWidgetAdapterViewId(widgetType),
            StackWidgetType.getStackWidgetEmptyViewId(widgetType)
        )
        remoteViews.setPendingIntentTemplate(
            StackWidgetType.getStackWidgetAdapterViewId(widgetType),
            StackWidgetType.getPendingIntentTemplate(pendingIntentFactory, widgetType, habits)
        )
        return remoteViews
    }
}
