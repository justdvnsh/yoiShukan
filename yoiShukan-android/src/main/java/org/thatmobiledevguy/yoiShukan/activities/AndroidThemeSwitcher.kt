package org.thatmobiledevguy.yoiShukan.activities

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build.VERSION.SDK_INT
import androidx.core.content.ContextCompat
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.core.preferences.Preferences
import org.thatmobiledevguy.yoiShukan.core.ui.ThemeSwitcher
import org.thatmobiledevguy.yoiShukan.core.ui.views.DarkTheme
import org.thatmobiledevguy.yoiShukan.core.ui.views.LightTheme
import org.thatmobiledevguy.yoiShukan.core.ui.views.PureBlackTheme
import org.thatmobiledevguy.yoiShukan.core.ui.views.Theme
import org.thatmobiledevguy.yoiShukan.inject.ActivityContext
import org.thatmobiledevguy.yoiShukan.inject.ActivityScope

@ActivityScope
class AndroidThemeSwitcher
constructor(
    @ActivityContext val context: Context,
    preferences: Preferences
) : ThemeSwitcher(preferences) {

    override var currentTheme: Theme = LightTheme()

    override fun getSystemTheme(): Int {
        if (SDK_INT < 29) return THEME_LIGHT
        val uiMode = context.resources.configuration.uiMode
        return if ((uiMode and UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES) {
            THEME_DARK
        } else {
            THEME_LIGHT
        }
    }

    override fun applyDarkTheme() {
        currentTheme = DarkTheme()
        context.setTheme(R.style.AppBaseThemeDark)
        (context as Activity).window.navigationBarColor =
            ContextCompat.getColor(context, R.color.grey_900)
    }

    override fun applyLightTheme() {
        currentTheme = LightTheme()
        context.setTheme(R.style.AppBaseTheme)
    }

    override fun applyPureBlackTheme() {
        currentTheme = PureBlackTheme()
        context.setTheme(R.style.AppBaseThemeDark_PureBlack)
        (context as Activity).window.navigationBarColor =
            ContextCompat.getColor(context, R.color.black)
    }
}
