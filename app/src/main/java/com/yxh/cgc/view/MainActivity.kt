package com.yxh.cgc.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import com.yxh.cgc.R
import com.yxh.cgc.base.BaseActivity
import com.yxh.cgc.databinding.ActivityMainBinding
import com.yxh.cgc.service.CustomService
import com.yxh.cgc.utils.PreventFastClickListener
import com.yxh.cgc.utils.setPreventFastClickListener
import com.yxh.cgc.view.vm.MainActivityVM

class MainActivity : BaseActivity<MainActivityVM, ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        binding.vm = viewModel

        // 必须同意获取悬浮窗权限才能实现应用自启动
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse("package:${packageName}")
            startActivity(intent)
        }

        checkPermission()

        binding.toSerialPortActivity.setPreventFastClickListener(object :
            PreventFastClickListener() {
            override fun onPreventFastClick(v: View?) {
                startActivity(Intent(this@MainActivity, SerialPortActivity::class.java))
            }
        })

        binding.toChatActivity.setPreventFastClickListener(object :
            PreventFastClickListener() {
            override fun onPreventFastClick(v: View?) {
                val p0 = Intent(this@MainActivity, ChatActivity::class.java)
                p0.data = (Uri.parse("cgc://com.yxh.cgc?seq=null"))
                startActivity(p0)
            }
        })

        binding.stopService.setOnClickListener {
            PromptDialog(object : PromptDialogClickListener{
                override fun onClick(dialog: PromptDialog, action: Int) {
                    if (action == 1) {
                        application.stopService(Intent(application, CustomService::class.java))
                    }
                    dialog.dismiss()
                }

            }).show(supportFragmentManager, "")
        }

    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.QUERY_ALL_PACKAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.QUERY_ALL_PACKAGES), 201
                )
            }
        }
    }
}