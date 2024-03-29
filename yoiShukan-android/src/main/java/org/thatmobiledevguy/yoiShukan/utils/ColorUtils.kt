package org.thatmobiledevguy.yoiShukan.utils

import android.graphics.Color
import kotlin.math.max

object ColorUtils {
    private const val ALPHA_CHANNEL = 24
    private const val RED_CHANNEL = 16
    private const val GREEN_CHANNEL = 8
    private const val BLUE_CHANNEL = 0

    @JvmStatic
    fun mixColors(color1: Int, color2: Int, amount: Float): Int {
        val a = mixColorChannel(color1, color2, amount, ALPHA_CHANNEL)
        val r = mixColorChannel(color1, color2, amount, RED_CHANNEL)
        val g = mixColorChannel(color1, color2, amount, GREEN_CHANNEL)
        val b = mixColorChannel(color1, color2, amount, BLUE_CHANNEL)
        return a or r or g or b
    }

    @JvmStatic
    fun setAlpha(color: Int, newAlpha: Float): Int {
        val intAlpha = (newAlpha * 255).toInt()
        return Color.argb(intAlpha, Color.red(color), Color.green(color), Color.blue(color))
    }

    @JvmStatic
    fun setMinValue(color: Int, newValue: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = max(hsv[2], newValue)
        return Color.HSVToColor(hsv)
    }

    private fun mixColorChannel(color1: Int, color2: Int, amount: Float, channel: Int): Int {
        val fl = (color1 shr channel and 0xff).toFloat() * amount
        val f2 = (color2 shr channel and 0xff).toFloat() * (1.0f - amount)
        return (fl + f2).toInt() and 0xff shl channel
    }
}
