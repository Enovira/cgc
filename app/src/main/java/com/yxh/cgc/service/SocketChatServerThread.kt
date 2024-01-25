package com.yxh.cgc.service

import com.blankj.utilcode.util.LogUtils
import com.yxh.cgc.utils.TimerManager
import com.yxh.cgc.utils.tryRead
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.TimerTask

/**
 * @description 线程: Socket服务端
 */
class SocketChatServerThread(
    private val serverSocket: ServerSocket,
    private val commandCallback: (String?) -> Unit,
) : Thread() {
    private var socket: Socket? = null
    private var timerManager: TimerManager? = null
    private var lastReceivedResponsePacketTime: Long = 0

    private var bufferedReader: BufferedReader? = null
    private var outputStream: OutputStream? = null

    override fun run() {
        LogUtils.d("正在创建socket服务端")
        while (!interrupted()) {
            try {
                socket = serverSocket.accept()
                LogUtils.d("socket连接成功")
                commandCallback.invoke(null) // 通过回调函数，开启串口
//            startHeartBeatTimer() // 开启心跳计时器
                socket?.getInputStream()?.also {
                    bufferedReader = BufferedReader(InputStreamReader(it))
                }
                outputStream = socket?.getOutputStream()
                while (socket?.isConnected == true) {
                    bufferedReader?.let { reader ->
                        handleMessage(reader.readLine())
                    }
                }
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                LogUtils.d("异常断开")
                socket?.close()
            }
        }
    }

    /**
     * 通过socket发送数据
     * @param str 字符串
     */
    fun sendMessage(str: String) {
        Thread {
            try {
                outputStream?.write(str.toByteArray(StandardCharsets.UTF_8))
                outputStream?.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun startHeartBeatTimer() {
        if (timerManager == null) {
            timerManager = TimerManager()
        }
        lastReceivedResponsePacketTime = System.currentTimeMillis()
        timerManager?.startTimer(0, 10000, object : TimerTask() {
            override fun run() {
                println("发送心跳包------")
                sendMessage("0")
                if (System.currentTimeMillis() - lastReceivedResponsePacketTime > 35000) {
                    timerManager?.clearTimer()
                    timerManager = null
                }
            }
        })
    }

    /**
     * 通过socket发送数据
     * @param bytes 字节数组
     */
    fun sendMessage(bytes: ByteArray) {
        Thread {
            try {
                outputStream?.run {
                    write(bytes)
                    flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun disconnect() {
        println("手动结束socket连接=========================================")
        interrupt()
        outputStream?.flush()
        outputStream?.close()
        bufferedReader?.close()
        socket?.close()
    }

    private fun handleMessage(str: String) {
        try {
            commandCallback.invoke(str)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun mergeByteArray(firstBytes: ByteArray, secondBytes: ByteArray, len: Int): ByteArray {
        val bytes = ByteArray(firstBytes.size + len)
        System.arraycopy(firstBytes, 0, bytes, 0, firstBytes.size)
        System.arraycopy(secondBytes, firstBytes.size, bytes, 0, len)
        return bytes
    }
}