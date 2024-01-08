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
package org.thatmobiledevguy.yoiShukan.core.commands

import org.thatmobiledevguy.yoiShukan.core.BaseUnitTest
import org.thatmobiledevguy.yoiShukan.core.models.Entry
import org.thatmobiledevguy.yoiShukan.core.models.Habit
import org.thatmobiledevguy.yoiShukan.core.models.Timestamp
import org.thatmobiledevguy.yoiShukan.core.utils.DateUtils.Companion.getToday
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CreateRepetitionCommandTest : BaseUnitTest() {
    private lateinit var command: CreateRepetitionCommand
    private lateinit var habit: Habit
    private lateinit var today: Timestamp

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        habit = fixtures.createShortHabit()
        habitList.add(habit)
        today = getToday()
        command = CreateRepetitionCommand(habitList, habit, today, 100, "")
    }

    @Test
    fun testExecute() {
        val entries = habit.originalEntries
        var entry = entries.get(today)
        assertEquals(Entry.YES_MANUAL, entry.value)
        command.run()
        entry = entries.get(today)
        assertEquals(100, entry.value.toLong())
    }
}
