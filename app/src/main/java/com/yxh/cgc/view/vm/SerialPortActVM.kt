package com.yxh.cgc.view.vm

import android.app.Application
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.yxh.cgc.bean.CommandBean
import com.yxh.cgc.utils.PreventFastClickListener
import com.yxh.cgc.utils.SerialPortManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.keepempty.sph.library.SerialPortConfig
import top.keepempty.sph.library.SerialPortHelper
import top.keepempty.sph.library.SphCmdEntity
import top.keepempty.sph.library.SphResultCallback
import java.nio.ByteBuffer
import java.nio.charset.Charset

class SerialPortActVM(private val application: Application) : AndroidViewModel(application) {

    var serialPortStatus: MutableLiveData<Boolean> = MutableLiveData(false)

    var sentMessageContent: MutableLiveData<String> = MutableLiveData()
    var receivedMessageContent: MutableLiveData<String> = MutableLiveData()

    private var serialPortHelper: SerialPortHelper? = null

    private val endPoint = "}".toByteArray(Charset.defaultCharset())
    private var count: Int = 0

    fun initSerialPortHelper() {
        val receivedData: StringBuilder = StringBuilder()
        val byteBuffer: ByteBuffer = ByteBuffer.allocate(1024)
        serialPortHelper = SerialPortHelper(32)
        val serialPortConfig = SerialPortConfig()
        serialPortConfig.path = "dev/ttyVIZ3"
        serialPortHelper?.setConfigInfo(serialPortConfig)
        serialPortHelper?.setSphResultCallback(object : SphResultCallback {
            override fun onSendData(sendCom: SphCmdEntity?) {
                sendCom?.let {
//                    println("发送命令: ${String(it.commands)}")
                }
            }

            override fun onReceiveData(data: SphCmdEntity?) {
                data?.let {
                    byteBuffer.put(it.commands)
                    count += 1
                    if (it.commands.contentEquals(endPoint)) {
//                        println(String(byteBuffer.array()))
                        val resultByte = ByteArray(byteBuffer.position())
                        System.arraycopy(byteBuffer.array(), 0, resultByte, 0, resultByte.size)
                        byteBuffer.flip()
                    }
                }
            }

            override fun onComplete() {
            }
        })
    }

    // 切换串口状态
    fun switchSerialPort(v: View?) {
        v?.isEnabled = false
        viewModelScope.launch {
            SerialPortManager.instance().toggleDevice().run {
                val text = when (this) {
                        1 -> {
                            serialPortStatus.value = true
                            "开启串口成功"
                        }
                        2 -> {
                            serialPortStatus.value = false
                            "关闭串口成功"
                        }
                        else -> "开启串口失败"
                    }
                LogUtils.d("串口操作状态: $text")
            }
            delay(1000)
//            withContext(Dispatchers.Main) {
                v?.isEnabled = true
//            }
        }
    }

    fun doMessageAfterTextChanged(editable: Editable) {
        sentMessageContent.postValue(editable.toString())
    }

    fun sendMessage() {
//        if (serialPortStatus.value != true) {
//            Toast.makeText(getApplication(), "请先开启串口", Toast.LENGTH_SHORT).show()
//            return
//        }

//        val str = sentMessageContent.value
//        if (str == null) {
//            Toast.makeText(getApplication(), "请输入要传输的信息", Toast.LENGTH_SHORT).show()
//            return
//        }
        val commandBean = CommandBean(SentCommand.scaleWeight.command)
        val str = Gson().toJson(commandBean)
        SerialPortManager.instance().sendData(str)
//        // 发送数据实体
//        val comEntry = SphCmdEntity()
//        comEntry.commands = str.toByteArray(Charset.defaultCharset()) // 发送命令字节数组
//        comEntry.flag = 12 // 备用标识
//        comEntry.commandsHex = str // 发送十六进制字符串
//        comEntry.timeOut = 100 // 超时时间 ms
//        comEntry.reWriteCom = false // 超时是否重发 默认false
//        comEntry.reWriteTimes = 5 // 重发次数
//        comEntry.receiveCount = 1 // 接收数据条数，默认为1
//        serialPortHelper?.addCommands(comEntry)
//        println("发送消息: $str")
    }

    private enum class SentCommand(val command: String) {
        scaleWeight("scale_weight"), // 秤-重量
        scaleClear("scale_clear"), // 秤-去皮
        snap("snap"), // 快门

        // ---------指纹相关-----------------------
        //移动端登记流程，需要发起2次接口进行登记。
        //第2次登记接口才返回具体登记后的指纹特征点数据。
        //指纹登记/采集指令，在5秒期间内不断进行登记/采集，直到成功，若在5秒后超时则返回失败。若收到取消指纹操作的命令，则取消登记/采集操作。
        enrol1("enrol_1"), // 第一次登记
        enrol2("enrol_2"), // 第二次登记
        cancelFp("cancel_fp"), // 取消指纹登记/采集
        capture("capture"), // 指纹采集
    }

}