package com.yxh.cgc

import android.app.Application
import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.LogUtils
import com.yxh.cgc.global.EventViewModel
import com.yxh.cgc.global.UncaughtExceptionHandler
import java.io.File
import java.lang.ref.WeakReference

class App : Application(), ViewModelStoreOwner {

    private lateinit var mViewModelStore: ViewModelStore
    lateinit var eventVM: EventViewModel
    private val path = Environment.getExternalStorageDirectory().path + "/cgc/logs" + File.separator

    companion object {
        private lateinit var instance: App
        private lateinit var context: WeakReference<Context>
        fun instance(): App {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = WeakReference(applicationContext)
        mViewModelStore = ViewModelStore()
        eventVM = ViewModelProvider(this)[EventViewModel::class.java]
        UncaughtExceptionHandler.initUncaughtExceptionHandler(this)

        LogUtils.getConfig().isLogSwitch = true //日志总开关
        LogUtils.getConfig().isLog2FileSwitch = false //文件总开关
        LogUtils.getConfig().dir = path
    }

    fun getContext(): Context {
        return context.get()!!
    }

    override val viewModelStore: ViewModelStore
        get() = mViewModelStore
}