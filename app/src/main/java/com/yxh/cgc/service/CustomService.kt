package com.yxh.cgc.service

import android.app.Notification.Action
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.yxh.cgc.App
import com.yxh.cgc.R
import com.yxh.cgc.bean.SocketConfig
import com.yxh.cgc.global.Cons
import com.yxh.cgc.utils.CustomNetworkUtil
import com.yxh.cgc.utils.SerialPortManager
import com.yxh.cgc.view.ChatActivity
import java.lang.Exception
import java.net.ServerSocket

class CustomService : Service() {

    private var chatServerThread: SocketChatServerThread? = null
    private lateinit var serverSocket: ServerSocket

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        serverSocket = ServerSocket(Cons.serverSocketPort)
        mHandler.sendEmptyMessage(1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                "com.yxh.cgc.startService" -> {
                    val seq = it.getStringExtra("message") ?: "null"
                    clearChatServerThread()
                    if (chatServerThread == null) {
                        chatServerThread =
                            SocketChatServerThread(serverSocket , object : ((String?) -> Unit) {
                                override fun invoke(p1: String?) {
                                    p1?.let {
                                        mHandler.sendMessage(mHandler.obtainMessage(0, p1))
                                    } ?: kotlin.run {
                                        mHandler.sendEmptyMessage(1)
                                    }
                                }
                            })
                        chatServerThread?.start()
                        startCustomForegroundService(seq)
                    }
                    packageManager.getLaunchIntentForPackage("com.dlxx.mam.Internal")?.let { p1 ->
                        p1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        try {
                            startActivity(p1)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
//                    launch3rdAppResult(Cons.serverSocketPort, seq)
                }

                "com.yxh.cgc.sendMessage" -> {
                    val message = it.getStringExtra("message") ?: ""
                    chatServerThread?.sendMessage(message)
                }

                else -> {}
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startCustomForegroundService(seq: String) {
        val cmm = getSystemService(NotificationManager::class.java)
        val notificationChannel = NotificationChannel(
            "customChannelId",
            "cgcNotification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        cmm.createNotificationChannel(notificationChannel)
        val notification = NotificationCompat.Builder(this, "customChannelId")
            .setSmallIcon(R.mipmap.ic_launcher_round)
//            .setContentTitle("cgcCustomService")
            .setContentTitle("socketService?seq=$seq")
            .setContentText("仓管车socket服务正在运行中。。。${System.currentTimeMillis()}")
            .setTicker("titleBar's title") //设置状态栏的标题
            .setAutoCancel(true) //打开程序后图标消失
            .setOngoing(true).apply {
                val i1 = Intent(this@CustomService, ChatActivity::class.java)
                val p1 = PendingIntent.getActivity(
                    this@CustomService,
                    201,
                    i1,
                    PendingIntent.FLAG_IMMUTABLE
                )
                setContentIntent(p1)
            }.build()
        startForeground(1, notification)
    }

    private fun launch3rdAppResult(port: Int, seq: String) {
//        val ipAddress = CustomNetworkUtil.instance.getIpAddress()
//        if (ipAddress == null) {
//            Toast.makeText(this, "无法获取ip地址，请检查网络连接", Toast.LENGTH_SHORT).show()
//        }
//        ipAddress?.let {
//            println("$it:$port && seq=$seq")
//            val i1 = Intent()
//            i1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            i1.data =
//                Uri.parse("wxworklocal://jsapi/requst3rdapp_result?errcode=0&seq=$seq&data={\"ip\":\"$it\",\"port\":\"$port\"}")
//            App.instance().eventVM.socketConfig.postValue(SocketConfig(it, port))
//            if (i1.resolveActivity(packageManager) != null) {
//                startActivity(i1)
//            } else {
//                Toast.makeText(this, "未找到符合的目标程序", Toast.LENGTH_SHORT).show()
//            }
//        }
        val ipAddress = "127.0.0.1" //固定IP地址
        val i1 = Intent()
        i1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i1.data =
            Uri.parse("wxworklocal://jsapi/requst3rdapp_result?errcode=0&seq=$seq&data={\"ip\":\"$ipAddress\",\"port\":\"$port\"}")
        App.instance().eventVM.socketConfig.postValue(SocketConfig(ipAddress, port))
        if (i1.resolveActivity(packageManager) != null) {
            startActivity(i1)
        } else {
            Toast.makeText(this, "未找到符合的目标程序", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serverSocket.close()
        clearChatServerThread()
    }

    private fun clearChatServerThread() {
        chatServerThread?.disconnect()
        chatServerThread = null
    }

    /**
     * 创建回调函数，将通过串口接收到的数据(通过socket)传输到i国网
     */
    private val listener = object : (ByteArray) -> Unit {
        override fun invoke(p1: ByteArray) {
            chatServerThread?.sendMessage(p1)
        }
    }

    private val mHandler: Handler = Handler(Looper.getMainLooper()) {
        if (it.what == 0) { //采集指令，此处只做透传
            SerialPortManager.instance().sendData(it.obj.toString())
//            val command = String(it.obj as ByteArray)
//            if (command.contains("weight")) {
//                chatServerThread?.sendMessage("{\n\"type\":\"scale_weight\"\n\"result\": 0.0\n\"power\"10}")
//            } else if (command.contains("clear")) {
//                chatServerThread?.sendMessage("{\n\"type\":\"scale_clear\"\n\"result\": success\n\"power\"10}")
//            } else if (command.contains("enrol_1")) {
//                chatServerThread?.sendMessage("{\n\"type\":\"enrol_1\"\n\"result\": success\n}")
//            } else if (command.contains("enrol_2")) {
//                chatServerThread?.sendMessage("{\n\"type\":\"enrol_2\"\n\"result\": success\n\"data\": \"03015a128c0080028002800280028002800280028002800280028006801e801e801ec01ec01ec01e0000000000000000000000000000000018871ffe1097a016309cddbe3d22835e14289cbe4aaed95e14ba1ade6c1c25bf4c202bdf1521c5df1f255c5f2531843f3dc0197f1da105dc55276afa57a516bb4a92dff45414635500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000301590f8900c0028002800280028002800280028002800280028006807e807e807e807ec0fec0fe000000000000000000000000000000001886e05e309c5d7e3d22835e13a8dd1e4aae991e6b9c65bf4c202bdf1521c5df1f255c5f2531841f1739daff1da105fc55276afa57a5169b4b12dfb00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\"\n}")
//            } else if (command.contains("cancel_fp")) {
//                chatServerThread?.sendMessage("{\n\"type\":\"cancel_fp\"\n\"result\": success\n}")
//            } else if (command.contains("capture")) {
//                chatServerThread?.sendMessage("{\n\"type\":\"capture\"\n\"result\": success\n\"data\": \"03014f1a8e00001e000e000600060006000200020002000000000000000000000000000080008000000000000000000000000000000000005188623e489a627e3e20233e73a04c9e522d661e2c38165e3c0fa13f0d93099f602863df22a9a99f722a4d5f31ac945f252e6b3f662ea4df71afcd3f0d9908bd71a54cbd723a4f7d6838a3fa239ce11b6fb58f786a350f330d3b00d61d9d5f771d9fe4d51a1ddd9200000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003014b198d008002800280028002800080008000800080008000c000c000c000e000e002f002f8064c1200000000000000000000000000006a0aa3be5320d6be4b3f173e34939a9f249f99df1da39a1f2ea9c17f503316ff38b5177f4c38c05f26b9811f3d3ec13f72100e1d4d12aa7d72138ffd3298c21d3e17417a189e037b3a995778731bd4f85c9654995a1a29b91c9c039973a414ac4a88dd6d00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\"\n}")
//            }
        } else if (it.what == 1) { //开启串口
            SerialPortManager.instance().openDeviceSerialPort(listener)
        }
        false
    }
}