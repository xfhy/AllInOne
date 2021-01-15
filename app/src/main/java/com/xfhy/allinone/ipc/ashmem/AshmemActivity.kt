package com.xfhy.allinone.ipc.ashmem

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.os.Parcel
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_ashmem.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileReader

/**
 * 2021年01月14日07:21:39
 * Ashmem 匿名共享内存(一种IPC) test
 *
 * 在使用Binder在进程间传递数据的时候,有时候会抛出TransactionTooLargeException这个异常,
 * 这个异常的产生是因为Binder驱动对内存的限制引起的.也就是说,我们不能通过Binder传递太大的数据.官方文档有说明,最大通常限制为1M-8k.
 *
 *  但是,有一个问题.在Android系统中,APP端View视图的数据是如何传递给SurfaceFlinger服务的呢?
 *  View绘制的数据最终是按照一帧一帧显示到屏幕上的,而每一帧都会占用一定的存储空间,在APP端执行draw的时候,
 *  数据很明显是要绘制到APP的进程空间,但是视图窗口要经过SurfaceFlinger图层混排才会生成最终的帧,
 *  而SurfaceFlinger又运行在另一个独立的服务进程,那么View视图的数据是如何在两个进程间传递的呢?
 *  普通的Binder通信肯定不行,因为Binder不太适合这种数据量大的通信,那么View数据的通信采用的是什么IPC手段呢?
 *  答案是匿名共享内存(Anonymous Shared Memory-Ashmem).
 *
 */
private const val TAG = "xfhy_ashmem"

class AshmemActivity : TitleBarActivity() {

    private var mService: IBinder? = null

    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }

    override fun getThisTitle(): CharSequence {
        return "Ashmem"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ashmem)

        btnBindAshmemService.setOnClickListener {
            bindAshmemService()
        }
        btnShareText.setOnClickListener {
            getShareText()
        }
        btnShareBitmap.setOnClickListener {
            getShareBitmap()
        }
    }

    private fun bindAshmemService() {
        Intent().apply {
            action = "com.xfhy.ashmem.Server.Action"
            setPackage("com.xfhy.allinone")
        }.also { intent ->
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun getShareText() {
        log(TAG, "Client : onServiceConnected")
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            //通过binder机制跨进程调用服务端的接口
            log(TAG, "Client : call server method code = $SHARE_TEXT_CODE")
            mService?.transact(SHARE_TEXT_CODE, data, reply, 0)
            //获得RemoteService创建的匿名共享内存的fd
            val fileDescriptor = reply.readFileDescriptor().fileDescriptor
            //获取匿名共享内存中的数据,并打印log
            val bufferedReader = BufferedReader(FileReader(fileDescriptor))
            val readLine = bufferedReader.readLine()
            log(TAG, "Client : readText = $readLine")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getShareBitmap() {
        log(TAG, "Client : onServiceConnected")
        val data = Parcel.obtain()
        val reply = Parcel.obtain()
        try {
            //通过binder机制跨进程调用服务端的接口
            log(TAG, "Client : call server method code = $SHARE_BITMAP_CODE")
            mService?.transact(SHARE_BITMAP_CODE, data, reply, 0)
            //获得RemoteService创建的匿名共享内存的fd
            val fileDescriptor = reply.readFileDescriptor().fileDescriptor

            //获取匿名共享内存中的数据,并设置到ImageView上
            val fileInputStream = FileInputStream(fileDescriptor)
            val readBytes = fileInputStream.readBytes()
            if (readBytes.isNotEmpty()) {
                val bitmap = BitmapFactory.decodeByteArray(readBytes, 0, readBytes.size)
                bitmap?.let {
                    //正常情况下,这里应该压缩一下,因为你不知道这个Bitmap有多大,太大了直接展示的话可能会崩溃
                    ivAshmemShare.setImageBitmap(bitmap)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

}
