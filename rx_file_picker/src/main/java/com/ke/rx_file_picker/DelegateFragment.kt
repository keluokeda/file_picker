package com.ke.rx_file_picker


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import io.reactivex.subjects.PublishSubject


class DelegateFragment : Fragment() {


    lateinit var pickResultSubject: PublishSubject<PickResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun start() {


        pickResultSubject = PublishSubject.create()

        val permission = ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startPick()
        } else {
            requestCameraPermission()
        }
    }


    private fun requestCameraPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val grantResult = grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (grantResult) {
            startPick()
        } else {
//            pickResultSubject.onNext(ScanResult(status = ScanResultStatus.NoCameraPermission))

            pickResultSubject.onNext(PickResult(null))
            pickResultSubject.onComplete()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_FILE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data

                pickResultSubject.onNext(PickResult(uri))
                pickResultSubject.onComplete()

            } else if (resultCode == Activity.RESULT_CANCELED) {
//                pickResultSubject.onNext(ScanResult(status = ScanResultStatus.Cancel))

                pickResultSubject.onNext(PickResult(null))
                pickResultSubject.onComplete()
            }
        }
    }


    private fun startPick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.type = "*/*"

        intent.addCategory(Intent.CATEGORY_OPENABLE)


        startActivityForResult(intent, REQUEST_CODE_PICK_FILE)
    }


    companion object {
        private const val REQUEST_CODE_PICK_FILE = 1001
    }
}
