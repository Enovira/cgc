package com.yxh.cgc.utils

import com.blankj.utilcode.util.LogUtils
import top.keepempty.sph.library.DataConversion
import top.keepempty.sph.library.SerialPortConfig
import top.keepempty.sph.library.SerialPortHelper
import top.keepempty.sph.library.SphCmdEntity
import top.keepempty.sph.library.SphResultCallback
import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Timer
import java.util.TimerTask


class SerialPortManager {
    private var serialPortHelper: SerialPortHelper? = null
    private val byteBuffer: ByteBuffer by lazy { ByteBuffer.allocate(1024) }
    private var lastReceivedDataTime: Long = 0
    private var isOpened = false
    private var listener: ((ByteArray) -> Unit)? = null
    private var timer: Timer? = null

    companion object {
        private var _instance: SerialPortManager? = null
        fun instance(): SerialPortManager {
            if (_instance == null) {
                synchronized(SerialPortManager::class.java) {
                    if (_instance == null) {
                        _instance = SerialPortManager()
                    }
                }
            }
            return _instance!!
        }
    }

    init {
        serialPortHelper = SerialPortHelper(32)
        val serialPortConfig = SerialPortConfig()
        serialPortConfig.path = "dev/ttyVIZ3"
        serialPortConfig.baudRate = 9600
        serialPortHelper?.setConfigInfo(serialPortConfig)
        serialPortHelper?.setSphResultCallback(object : SphResultCallback {
            override fun onSendData(sendCom: SphCmdEntity?) {
                sendCom?.let {
                    LogUtils.d("发送命令给底座: ${String(it.commands)}")
                }
            }

            override fun onReceiveData(data: SphCmdEntity?) {
                data?.let {
                    byteBuffer.put(it.commands)
                    lastReceivedDataTime = System.currentTimeMillis()
                    if (timer == null) {
                        startTimer()
                    }
                }
            }

            override fun onComplete() {
            }
        })
    }

    fun toggleDevice(): Int {
        if (isOpened) {
            serialPortHelper?.closeDevice()
            isOpened = false
            return 2
        } else {
            return if (serialPortHelper?.openDevice() == true) {
                isOpened = true
                1
            } else {
                -1
            }
        }
    }

    fun openDeviceSerialPort(unit: (ByteArray) -> Unit) {
        listener = unit
        if (serialPortHelper?.openDevice() == true) {
            LogUtils.d("开启串口成功")
            isOpened = true
        } else {
            LogUtils.d("开启串口失败")
        }
    }

    fun sendData(str: String) {
        val bytes = str.toByteArray(Charset.defaultCharset())
        // 发送数据实体
        val comEntry = SphCmdEntity()
        comEntry.commands = bytes // 发送命令字节数组
        comEntry.flag = 1 // 备用标识
        comEntry.commandsHex = DataConversion.encodeHexString(bytes) // 发送十六进制字符串
        comEntry.timeOut = 10 // 超时时间 ms
        comEntry.reWriteCom = false // 超时是否重发 默认false
        comEntry.reWriteTimes = 5 // 重发次数
        comEntry.receiveCount = 1 // 接收数据条数，默认为1
        serialPortHelper?.addCommands(comEntry)
//        startTimer()
    }

    fun sendData(bytes: ByteArray) {
        // 发送数据实体
        val comEntry = SphCmdEntity()
        comEntry.commands = bytes // 发送命令字节数组
        comEntry.flag = 1 // 备用标识
        comEntry.commandsHex = DataConversion.encodeHexString(bytes) // 发送十六进制字符串
        comEntry.timeOut = 10 // 超时时间 ms
        comEntry.reWriteCom = false // 超时是否重发 默认false
        comEntry.reWriteTimes = 5 // 重发次数
        comEntry.receiveCount = 1 // 接收数据条数，默认为1
        serialPortHelper?.addCommands(comEntry)
//        startTimer()
    }

    private fun startTimer() {
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (System.currentTimeMillis() - lastReceivedDataTime > 50) {
                    val bytes = ByteArray(byteBuffer.position())
                    byteBuffer.flip()
                    System.arraycopy(byteBuffer.array(), 0, bytes, 0, bytes.size)
                    LogUtils.d("接收到结果: ${String(bytes)}")
                    listener?.invoke(bytes)
                    byteBuffer.clear()
                    clearTimer()
                }
            }
        }, 0, 20)
    }

    private fun clearTimer() {
        timer?.cancel()
        timer = null
    }
}