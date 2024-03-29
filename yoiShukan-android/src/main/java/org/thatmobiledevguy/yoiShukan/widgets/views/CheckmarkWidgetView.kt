package org.thatmobiledevguy.yoiShukan.widgets.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import org.thatmobiledevguy.yoiShukan.YoiShukanApplication
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.activities.common.views.RingView
import org.thatmobiledevguy.yoiShukan.activities.habits.list.views.toShortString
import org.thatmobiledevguy.yoiShukan.core.models.Entry.Companion.NO
import org.thatmobiledevguy.yoiShukan.core.models.Entry.Companion.SKIP
import org.thatmobiledevguy.yoiShukan.core.models.Entry.Companion.UNKNOWN
import org.thatmobiledevguy.yoiShukan.core.models.Entry.Companion.YES_AUTO
import org.thatmobiledevguy.yoiShukan.core.models.Entry.Companion.YES_MANUAL
import org.thatmobiledevguy.yoiShukan.core.preferences.Preferences
import org.thatmobiledevguy.yoiShukan.inject.HabitsApplicationComponent
import org.thatmobiledevguy.yoiShukan.utils.InterfaceUtils.getDimension
import org.thatmobiledevguy.yoiShukan.utils.PaletteUtils.getAndroidTestColor
import org.thatmobiledevguy.yoiShukan.utils.StyledResources
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class CheckmarkWidgetView : HabitWidgetView {
    var activeColor: Int = 0

    var percentage = 0f
    var name: String? = null
    private lateinit var ring: RingView
    private lateinit var label: TextView
    var entryValue = 0
    var entryState = 0
    var isNumerical = false
    private var preferences: Preferences? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    fun refresh() {
        if (backgroundPaint == null || frame == null) return
        val res = StyledResources(context)
        val bgColor: Int
        val fgColor: Int
        setShadowAlpha(0x4f)
        when (entryState) {
            YES_MANUAL, SKIP -> {
                bgColor = activeColor
                fgColor = res.getColor(R.attr.contrast0)
                backgroundPaint!!.color = bgColor
                frame!!.setBackgroundDrawable(background)
            }
            YES_AUTO, NO, UNKNOWN -> {
                bgColor = res.getColor(R.attr.cardBgColor)
                fgColor = res.getColor(R.attr.contrast60)
            }
            else -> {
                bgColor = res.getColor(R.attr.cardBgColor)
                fgColor = res.getColor(R.attr.contrast60)
            }
        }
        ring.setPercentage(percentage)
        ring.setColor(fgColor)
        ring.setBackgroundColor(bgColor)
        ring.setText(text)
        label.text = name
        label.setTextColor(fgColor)
        requestLayout()
        postInvalidate()
    }

    private val text: String
        get() = if (isNumerical) {
            (max(0, entryValue) / 1000.0).toShortString()
        } else {
            when (entryState) {
                YES_MANUAL, YES_AUTO -> resources.getString(R.string.fa_check)
                SKIP -> resources.getString(R.string.fa_skipped)
                UNKNOWN -> {
                    run {
                        if (preferences!!.areQuestionMarksEnabled) {
                            return resources.getString(R.string.fa_question)
                        } else {
                            resources.getString(R.string.fa_times)
                        }
                    }
                    resources.getString(R.string.fa_times)
                }
                NO -> resources.getString(R.string.fa_times)
                else -> resources.getString(R.string.fa_times)
            }
        }

    override val innerLayoutId: Int
        get() = R.layout.widget_checkmark

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (height >= width) {
            height = min(height, (width * 1.5).roundToInt())
        } else {
            width = min(width, height)
        }
        val textSize = min(0.175f * width, getDimension(context, R.dimen.smallTextSize))
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        if (isNumerical) {
            ring.setTextSize(textSize * 0.9f)
        } else {
            ring.setTextSize(textSize)
        }
        ring.setThickness(0.03f * width)
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }

    private fun init() {
        val appComponent: HabitsApplicationComponent =
            (context.applicationContext as YoiShukanApplication).component
        preferences = appComponent.preferences
        ring = findViewById<View>(R.id.scoreRing) as RingView
        label = findViewById<View>(R.id.label) as TextView
        ring.setIsTransparencyEnabled(true)
        if (isInEditMode) {
            percentage = 0.75f
            name = "Wake up early"
            activeColor = getAndroidTestColor(6)
            entryValue = YES_MANUAL
            refresh()
        }
    }
}
