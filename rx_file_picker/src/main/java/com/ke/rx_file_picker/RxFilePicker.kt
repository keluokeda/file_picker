package com.ke.rx_file_picker

import androidx.fragment.app.FragmentActivity
import io.reactivex.Observable

class RxFilePicker(activity: androidx.fragment.app.FragmentActivity) {

    private val tag = "rx_file_picker"

    private val delegateFragment: DelegateFragment

    init {
        val fragment = activity.supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            delegateFragment = DelegateFragment()
            activity.supportFragmentManager.beginTransaction().add(delegateFragment, tag)
                .commitNow()
        } else {
            delegateFragment = fragment as DelegateFragment
        }
    }


    fun pickFile(type: String = "*/*"): Observable<PickResult> {
        delegateFragment.type = type
        delegateFragment.start()
        return Observable.just(1)
            .flatMap {
                delegateFragment.pickResultSubject
            }
    }
}