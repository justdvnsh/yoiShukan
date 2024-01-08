package org.thatmobiledevguy.yoiShukan.activities

import org.thatmobiledevguy.yoiShukan.AndroidDirFinder
import org.thatmobiledevguy.yoiShukan.core.ui.screens.habits.list.ListHabitsBehavior
import org.thatmobiledevguy.yoiShukan.core.ui.screens.habits.show.ShowHabitMenuPresenter
import java.io.File
import javax.inject.Inject

class HabitsDirFinder @Inject
constructor(
    private val androidDirFinder: AndroidDirFinder
) : ShowHabitMenuPresenter.System, ListHabitsBehavior.DirFinder {

    override fun getCSVOutputDir(): File {
        return androidDirFinder.getFilesDir("CSV")!!
    }
}
