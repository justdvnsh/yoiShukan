package org.thatmobiledevguy.yoiShukan

import android.app.Activity

class BaseExceptionHandler(private val activity: Activity) : Thread.UncaughtExceptionHandler {

    private val originalHandler: Thread.UncaughtExceptionHandler? =
        Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread?, ex: Throwable?) {
        if (ex == null) return
        if (thread == null) return
        try {
            ex.printStackTrace()
            AndroidBugReporter(activity).dumpBugReportToFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        originalHandler?.uncaughtException(thread, ex)
    }
}
