package com.yxh.cgc.view.vm

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import com.yxh.cgc.R
import com.yxh.cgc.service.CustomService
import com.yxh.cgc.view.ChatActivity
import com.yxh.cgc.view.PromptDialog

class MainActivityVM(private val application: Application) : AndroidViewModel(application), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.startService -> {
                application.startService(Intent(application, CustomService::class.java).apply {
                    action = "com.yxh.cgc.startService"
                })
            }
            R.id.stopService -> {
                application.stopService(Intent(application, CustomService::class.java))
            }
        }
    }
}