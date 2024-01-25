package com.yxh.cgc.bean

/**
 * @param type 命令
 * @param result 命令是否执行成功
 */
class ResponseData(type: String, result: String) {
    var power: Float? = null
    var data: String? = null
}