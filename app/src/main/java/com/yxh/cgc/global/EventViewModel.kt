package com.yxh.cgc.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yxh.cgc.bean.SocketConfig

class EventViewModel: ViewModel() {

    var socketMessage = MutableLiveData<ByteArray>()
    var socketConfig = MutableLiveData<SocketConfig>()

}