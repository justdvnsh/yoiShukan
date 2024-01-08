/*
 * Copyright (C) 2016-2021 Álinson Santos Xavier <git@axavier.org>
 *
 * This file is part of Loop Habit Tracker.
 *
 * Loop Habit Tracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Loop Habit Tracker is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.thatmobiledevguy.yoiShukan.activities.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import org.thatmobiledevguy.yoiShukan.HabitsApplication
import org.thatmobiledevguy.yoiShukan.R
import org.thatmobiledevguy.yoiShukan.activities.AndroidThemeSwitcher
import org.thatmobiledevguy.yoiShukan.core.models.PaletteColor
import org.thatmobiledevguy.yoiShukan.databinding.SettingsActivityBinding
import org.thatmobiledevguy.yoiShukan.utils.setupToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = (application as HabitsApplication).component
        val themeSwitcher = AndroidThemeSwitcher(this, component.preferences)
        themeSwitcher.apply()

        val binding = SettingsActivityBinding.inflate(LayoutInflater.from(this))
        binding.root.setupToolbar(
            toolbar = binding.toolbar,
            title = resources.getString(R.string.settings),
            color = PaletteColor(11),
            theme = themeSwitcher.currentTheme
        )
        setContentView(binding.root)
    }
}
