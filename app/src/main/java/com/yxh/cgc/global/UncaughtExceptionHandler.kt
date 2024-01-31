package com.yxh.cgc.global

import android.app.ActivityManager
import android.app.Application
import android.os.Looper
import android.os.Process
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import kotlin.system.exitProcess

class UncaughtExceptionHandler: Thread.UncaughtExceptionHandler {

    override fun uncaughtException(t: Thread, e: Throwable) {
        LogUtils.e(e)
        defaultExceptionHandler?.uncaughtException(t, e) ?: kotlin.run {
            handlerException(e)
            try {
                Thread.sleep(3000)
                application?.getSystemService(ActivityManager::class.java)?.appTasks?.last()?.finishAndRemoveTask()
                Process.killProcess(Process.myPid())
                exitProcess(10)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private val instance: UncaughtExceptionHandler by lazy { UncaughtExceptionHandler() }
        private val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        private var application: Application? = null

        fun initUncaughtExceptionHandler(application: Application) {
            Thread.currentThread().uncaughtExceptionHandler = instance
            this.application = application
        }
    }

    private fun handlerException(e: Throwable?): Boolean {
        return if (e == null) {
            false
        } else {
            try {
                Thread{
                    Looper.prepare()
                    Toast.makeText(application, "仓管车辅助程序发生异常，即将退出", Toast.LENGTH_SHORT).show()
                    Looper.loop()
                }.start()
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}