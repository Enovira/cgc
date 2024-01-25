package com.yxh.cgc.utils

import android.view.View
import java.io.InputStream

fun View.setPreventFastClickListener(listener: PreventFastClickListener) {
    setOnClickListener(listener)
}

/**
 * 尝试从InputStream中读取一定大小的数据
 * @receiver Reader
 * @param byteArray ByteArray 数据
 * @param maxLength Int 最大数据长度
 * @param timeout Long 超时时间
 * @return Int 读取到的数据长度
 */
fun InputStream.tryRead(byteArray: ByteArray, maxLength: Int = byteArray.size, timeout: Long = 100): Int {
    val time = System.currentTimeMillis()
    while (available() < maxLength
        && System.currentTimeMillis() - time < timeout) {
        Thread.sleep(1)
    }
    val length = if (available() >= maxLength) maxLength else available()
    return read(byteArray, 0, length)
}