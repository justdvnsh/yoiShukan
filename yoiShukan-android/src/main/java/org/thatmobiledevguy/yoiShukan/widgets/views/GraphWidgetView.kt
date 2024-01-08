package org.thatmobiledevguy.yoiShukan.widgets.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.thatmobiledevguy.yoiShukan.R

class GraphWidgetView(context: Context?, val dataView: View) : HabitWidgetView(context) {
    private lateinit var title: TextView
    fun setTitle(text: String?) {
        title.text = text
    }

    override val innerLayoutId: Int
        get() = R.layout.widget_graph

    private fun init() {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dataView.layoutParams = params
        val innerFrame = findViewById<View>(R.id.innerFrame) as ViewGroup
        innerFrame.addView(dataView)
        title = findViewById<View>(R.id.title) as TextView
        title.visibility = VISIBLE
    }

    init {
        init()
    }
}
