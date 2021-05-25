package com.ke.file_picker

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.ke.rx_file_picker.FileUtil
import com.ke.rx_file_picker.RxFilePicker
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)







        button.setOnClickListener {

            RxFilePicker(this)
                .pickFile()
                .subscribe {
                    val fileName = FileUtil.getFileNameFromUri(it.uri, contentResolver)

                    AlertDialog.Builder(this)
                        .setTitle("选择文件结果")
                        .setMessage(
                            "uri = ${it.uri} " +
                                    ",authority = ${it.uri?.authority}," +
                                    "path = ${it.uri?.path}," +
                                    "display name = $fileName"
                        )
                        .setPositiveButton("保存到本地") { _, _ ->
                            it.uri?.apply {

                                val parent =
                                    File(getExternalFilesDir(null)!!, "/payload/")
                                if (!parent.exists()) {
                                    //创建文件夹
                                    parent.mkdir()
                                }
                                val file = FileUtil.saveUriToFile(
                                    this@MainActivity, this,
                                    parent, fileName!!
                                )
                                if (file != null) {
                                    openFile(file)
                                }
                            }
                        }
                        .show()
                }
        }


    }


    private fun openFile(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(Intent.createChooser(intent, "标题"))
    }

}
