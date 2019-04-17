package com.ke.rx_file_picker

import android.support.v4.app.FragmentActivity
import io.reactivex.Observable

class RxFilePicker(activity: FragmentActivity) {

    private val tag = "rx_file_picker"

    private val delegateFragment: DelegateFragment

    init {
        val fragment = activity.supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            delegateFragment = DelegateFragment()
            activity.supportFragmentManager.beginTransaction().add(delegateFragment, tag).commitNow()
        } else {
            delegateFragment = fragment as DelegateFragment
        }
    }


    fun pickFile(): Observable<PickResult> {

        delegateFragment.start()
        return Observable.just(1)
            .flatMap {
                delegateFragment.pickResultSubject
            }
    }
}