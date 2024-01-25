package com.yxh.cgc.view

import android.content.Intent
import android.net.Uri
import android.view.View
import com.yxh.cgc.R
import com.yxh.cgc.base.BaseActivity
import com.yxh.cgc.databinding.ActivityMainBinding
import com.yxh.cgc.utils.PreventFastClickListener
import com.yxh.cgc.utils.setPreventFastClickListener
import com.yxh.cgc.view.vm.MainActivityVM

class MainActivity : BaseActivity<MainActivityVM, ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {

        binding.vm = viewModel

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

    }
}