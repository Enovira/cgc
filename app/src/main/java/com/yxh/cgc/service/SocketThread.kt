package com.yxh.cgc.service

import com.yxh.cgc.global.Cons
import com.yxh.cgc.utils.tryRead
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketAddress
import java.nio.charset.StandardCharsets

class SocketThread(private val commandCallback: (ByteArray?) -> Unit) : Thread() {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    override fun run() {
        val serviceSocket = ServerSocket(Cons.serverSocketPort)
        while (true) {
                val client = serviceSocket.accept()
                try {
                    inputStream = client.getInputStream()
                    outputStream = client.getOutputStream()
                    inputStream?.let { inputStream ->
                        while (client.isConnected) {
                            while (inputStream.available() == 0) {
                                sleep(100)
                            }
                            val bytes = ByteArray(1024)
                            var count: Int
                            var resultBytes = ByteArray(0)
                            while (inputStream.tryRead(bytes).also { count = it } >= 0) {
                                resultBytes = mergeByteArray(resultBytes, bytes, count)
                            }
                            handleMessage(resultBytes)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    client.close()
                }
        }
    }

    private fun mergeByteArray(firstBytes: ByteArray, secondBytes: ByteArray, len: Int): ByteArray {
        val bytes = ByteArray(firstBytes.size + len)
        System.arraycopy(firstBytes, 0, bytes, 0, firstBytes.size)
        System.arraycopy(secondBytes, firstBytes.size, bytes, 0, len)
        return bytes
    }

    /**
     * 通过socket发送数据
     * @param str 字符串
     */
    fun sendMessage(str: String) {
        Thread {
            try {
                outputStream?.run {
                    val bytes = str.toByteArray(StandardCharsets.UTF_8)
                    println("socketChatThread sendMessage")
                    write(bytes)
                    flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun handleMessage(bytes: ByteArray) {
        try {
            commandCallback.invoke(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}