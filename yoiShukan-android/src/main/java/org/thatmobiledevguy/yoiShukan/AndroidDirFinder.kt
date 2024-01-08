package org.thatmobiledevguy.yoiShukan

import android.content.Context
import androidx.core.content.ContextCompat
import org.thatmobiledevguy.yoiShukan.inject.AppContext
import org.thatmobiledevguy.yoiShukan.utils.FileUtils
import java.io.File
import javax.inject.Inject

class AndroidDirFinder @Inject constructor(@param:AppContext private val context: Context) {
    fun getFilesDir(relativePath: String): File? {
        return FileUtils.getDir(
            ContextCompat.getExternalFilesDirs(context, null),
            relativePath
        )
    }
}
