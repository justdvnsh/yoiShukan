package org.thatmobiledevguy.yoiShukan.widgets.views

import android.content.Context
import android.view.View
import android.widget.TextView
import org.thatmobiledevguy.yoiShukan.R

class EmptyWidgetView(context: Context?) : HabitWidgetView(context) {
    private lateinit var title: TextView
    fun setTitle(text: String?) {
        title.text = text
    }

    override val innerLayoutId: Int
        get() = R.layout.widget_graph

    private fun init() {
        title = findViewById<View>(R.id.title) as TextView
        title.visibility = VISIBLE
    }

    init {
        init()
    }
}
