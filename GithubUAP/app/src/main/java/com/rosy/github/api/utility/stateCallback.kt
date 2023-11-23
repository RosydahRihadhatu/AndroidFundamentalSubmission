package com.rosy.github.api.utility

import android.view.View

interface stateCallback<T> {
    fun onSuccess(data: T)
    fun onLoading()
    fun onFail(message: String?)

    val visible: Int
        get() = View.VISIBLE
    val gone: Int
        get() = View.GONE
}