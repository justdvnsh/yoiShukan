package org.thatmobiledevguy.yoiShukan.utils

import android.content.Context
import android.util.AttributeSet
import org.jetbrains.annotations.Contract

object AttributeSetUtils {
    const val ISORON_NAMESPACE = "http://isoron.org/android"

    @JvmStatic
    fun getAttribute(
        context: Context,
        attrs: AttributeSet,
        name: String,
        defaultValue: String?
    ): String? {
        val resId = attrs.getAttributeResourceValue(ISORON_NAMESPACE, name, 0)
        if (resId != 0) return context.resources.getString(resId)
        val value = attrs.getAttributeValue(ISORON_NAMESPACE, name)
        return value ?: defaultValue
    }

    @JvmStatic
    fun getBooleanAttribute(
        context: Context,
        attrs: AttributeSet,
        name: String,
        defaultValue: Boolean
    ): Boolean {
        val boolText = getAttribute(context, attrs, name, null)
        return if (boolText != null) java.lang.Boolean.parseBoolean(boolText) else defaultValue
    }

    @JvmStatic
    @Contract("_,_,_,!null -> !null")
    fun getColorAttribute(
        context: Context,
        attrs: AttributeSet,
        name: String,
        defaultValue: Int?
    ): Int? {
        val resId = attrs.getAttributeResourceValue(ISORON_NAMESPACE, name, 0)
        return if (resId != 0) context.resources.getColor(resId) else defaultValue
    }

    @JvmStatic
    fun getFloatAttribute(
        context: Context,
        attrs: AttributeSet,
        name: String,
        defaultValue: Float
    ): Float {
        return try {
            val number = getAttribute(context, attrs, name, null)
            number?.toFloat() ?: defaultValue
        } catch (e: NumberFormatException) {
            defaultValue
        }
    }

    fun getIntAttribute(
        context: Context,
        attrs: AttributeSet,
        name: String,
        defaultValue: Int
    ): Int {
        val number = getAttribute(context, attrs, name, null)
        return number?.toInt() ?: defaultValue
    }
}
