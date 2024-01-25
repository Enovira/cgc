package com.yxh.cgc.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.yxh.cgc.service.CustomService
import java.net.Socket

/**
 * 通过i国网启动的页面
 */
class AuxiliaryActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val seq = intent.data?.getQueryParameter("seq") ?: "empty"
        startCustomServerOnForeground(seq)
        finish()
    }

    private fun startCustomServerOnForeground(seq: String) {
        val i1 = Intent(this, CustomService::class.java)
        i1.putExtra("seq", seq)
        startService(i1)
    }
}