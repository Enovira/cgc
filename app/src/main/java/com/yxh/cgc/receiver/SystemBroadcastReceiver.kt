package com.yxh.cgc.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yxh.cgc.view.MainActivity

class SystemBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> { // 监听系统启动广播
                //AndroidManifest中注册的启动页，默认为ManiActivity
                val bootIntent = Intent(context, MainActivity::class.java)
                bootIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(bootIntent)
            }
        }
    }
}