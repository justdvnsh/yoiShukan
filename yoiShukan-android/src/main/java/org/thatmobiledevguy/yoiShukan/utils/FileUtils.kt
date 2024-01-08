package org.thatmobiledevguy.yoiShukan.utils

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun File.copyTo(dst: File) {
    val inStream = FileInputStream(this)
    val outStream = FileOutputStream(dst)
    inStream.copyTo(outStream)
}

fun InputStream.copyTo(dst: File) {
    val outStream = FileOutputStream(dst)
    this.copyTo(outStream)
}

fun InputStream.copyTo(out: OutputStream) {
    var numBytes: Int
    val buffer = ByteArray(1024)
    while (this.read(buffer).also { numBytes = it } != -1) {
        out.write(buffer, 0, numBytes)
    }
}

object FileUtils {
    @JvmStatic
    fun getDir(potentialParentDirs: Array<File>, relativePath: String): File? {
        val chosenDir: File? = potentialParentDirs.firstOrNull { dir -> dir.canWrite() }
        if (chosenDir == null) {
            Log.e("FileUtils", "getDir: all potential parents are null or non-writable")
            return null
        }
        val dir = File("${chosenDir.absolutePath}/$relativePath/")
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e("FileUtils", "getDir: chosen dir does not exist and cannot be created")
            return null
        }
        return dir
    }

    @JvmStatic
    fun getSDCardDir(relativePath: String): File? {
        val parents = arrayOf(Environment.getExternalStorageDirectory())
        return getDir(parents, relativePath)
    }
}
