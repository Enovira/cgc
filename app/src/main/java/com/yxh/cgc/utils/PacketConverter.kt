package com.yxh.cgc.utils

import java.nio.charset.StandardCharsets

object PacketConverter {
    private val receivedHeadBytes = ByteConvertTool.hexString2ByteArray("C47C")
    private val footBytes = ByteConvertTool.hexString2ByteArray("03")
    private val sendHeadBytes = ByteConvertTool.hexString2ByteArray("C45C")

    private var bytes: ByteArray = ByteArray(0)
    private var count: Int = 0

    fun decode(byteArray: ByteArray): Int {
        bytes = byteArray
        count = 0
        try {
            val head = ByteArray(2)
            copyToDestinationByteArray(head)
            if (!head.contentEquals(receivedHeadBytes)) {
                return -1
            }
            val command = ByteArray(1)
            copyToDestinationByteArray(command)
            val foot = ByteArray(1)
            copyToDestinationByteArray(foot)
            if (!foot.contentEquals(footBytes)) {
                return -1
            }
            println("报文解码:\n" +
                    "报文头: ${ByteConvertTool.byteArray2HexString(head)}\n" +
                    "命令码: ${ByteConvertTool.byteArray2HexString(command)}\n" +
                    "报文尾: ${ByteConvertTool.byteArray2HexString(foot)}")

            return ByteConvertTool.byteArray2HexString(command).toInt()
        } catch (e: Exception) {
            return -1
        }
    }

    fun encode(data: String, status: Int): ByteArray{
        val dataBytes = data.toByteArray(StandardCharsets.UTF_8)
        val dataLength = dataBytes.size
        bytes = ByteArray(6 + dataLength)
        count = 0
        copyToFinalByteArray(sendHeadBytes)
        val statusBytes = ByteConvertTool.hexString2ByteArray(status.toString())
        copyToFinalByteArray(statusBytes)
        val dataLengthByteArray = ByteConvertTool.short2ByteArray(status.toShort())
        return bytes
    }

    private fun copyToDestinationByteArray(destinationBytes: ByteArray) {
        System.arraycopy(bytes, count, destinationBytes, 0, destinationBytes.size)
        count += destinationBytes.size
    }

    private fun copyToFinalByteArray(sourceByteArray: ByteArray) {
        System.arraycopy(sourceByteArray, 0, bytes, count, sourceByteArray.size)
        count += sourceByteArray.size
    }
}