package com.yxh.cgc.utils

import android.view.View

abstract class PreventFastClickListener : View.OnClickListener {
    private val period: Long = 600L
    private var lastClickTime: Long = 0
    override fun onClick(v: View?) {
        System.currentTimeMillis().run {
            if (this - lastClickTime > period) {
                onPreventFastClick(v)
                lastClickTime = this
            }
        }
    }

    abstract fun onPreventFastClick(v: View?)
}