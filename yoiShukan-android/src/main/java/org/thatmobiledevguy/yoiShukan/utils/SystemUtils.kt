package org.thatmobiledevguy.yoiShukan.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context

object SystemUtils {

    fun unlockScreen(activity: Activity) {
        val km = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        km.requestDismissKeyguard(activity, null)
    }
}
