package com.yxh.cgc.view

import androidx.core.widget.doAfterTextChanged
import com.yxh.cgc.R
import com.yxh.cgc.base.BaseActivity
import com.yxh.cgc.databinding.ActivitySerialPortBinding
import com.yxh.cgc.utils.SharedPreferenceUtil
import com.yxh.cgc.view.vm.SerialPortActVM

class SerialPortActivity: BaseActivity<SerialPortActVM, ActivitySerialPortBinding>() {

    private var showUi: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_serial_port
    }

    override fun initView() {
        showUi = SharedPreferenceUtil.getInstance().getInt("showUi", 0)
        if (showUi == 0) {
            println("未进行设置")
            SharedPreferenceUtil.getInstance().putInt("showUi", 1)
        } else {
            println("已进行设置")
            SharedPreferenceUtil.getInstance().putInt("showUi", 0)
        }
        binding.vm = viewModel
//        viewModel.initSerialPortHelper()
        initListener()
        binding.receivedMessageDisplay.doAfterTextChanged {

        }
    }

    private fun initListener() {

    }
}