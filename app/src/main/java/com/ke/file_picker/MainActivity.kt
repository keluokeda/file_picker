package com.ke.file_picker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ke.rx_file_picker.RxFilePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)








        button.setOnClickListener {

            RxFilePicker(this)
                .pickFile()
                .subscribe({
                    val s = it

                }, {
                    val s = it

                }, {
                    val s = ""
                })
        }


    }


}
