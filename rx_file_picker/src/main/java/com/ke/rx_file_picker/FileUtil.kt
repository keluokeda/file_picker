package com.ke.rx_file_picker

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.*

object FileUtil {


    /**
     * 根据uri获取文件名称
     */
    fun getFileNameFromUri(uri: Uri?, contentResolver: ContentResolver): String? {
        if (uri == null) {
            return null
        }

        val cursor =
            contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null, null, null
            )
        cursor?.moveToFirst()
        val displayName = cursor?.getString(0)
        cursor?.close()
        return displayName
    }

    /**
     * 保存文件到本地
     */
    fun saveUriToFile(context: Context, uri: Uri, parent: File, filename: String): File? {
        var file: File? = null
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        var success = false
        try {

            inputStream = context.contentResolver.openInputStream(uri)
            file = File(parent, filename)
            file.deleteOnExit()
            outputStream = FileOutputStream(file)
            if (inputStream != null) {
                copy(inputStream, outputStream)
                success = true
            }
        } catch (ignored: IOException) {
        } finally {
            try {
                inputStream?.close()
            } catch (ignored: IOException) {
            }
            try {
                outputStream?.close()
            } catch (ignored: IOException) {
                // If closing the output stream fails, we cannot be sure that the
                // target file was written in full. Flushing the stream merely moves
                // the bytes into the OS, not necessarily to the file.
                success = false
            }
        }
        return if (success) file else null
    }

    @Throws(IOException::class)
    private fun copy(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(4 * 1024)
        var bytesRead: Int
        while (`in`.read(buffer).also { bytesRead = it } != -1) {
            out.write(buffer, 0, bytesRead)
        }
        out.flush()
    }
}