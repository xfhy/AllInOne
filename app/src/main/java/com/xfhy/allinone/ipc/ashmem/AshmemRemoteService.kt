package com.xfhy.allinone.ipc.ashmem

import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import com.xfhy.allinone.App
import com.xfhy.allinone.R
import com.xfhy.library.ext.log
import com.xfhy.library.ext.toByteArray
import java.io.FileDescriptor


/**
 * @author : xfhy
 * Create time : 2021年01月14日07:27:44
 * Description : Ashmem 服务端Service
 */

private const val TAG = "xfhy_ashmem"
const val SHARE_TEXT_CODE = 1
const val SHARE_BITMAP_CODE = 2

class AshmemRemoteService : Service() {

    class AshmemBinder : Binder() {
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            log(TAG, "Server : onTransact code=$code")
            return when (code) {
                SHARE_TEXT_CODE -> {
                    try {
                        val str = "xfhy"
                        log(TAG, "Server : share text=$str")
                        val contentBytes = str.toByteArray()
                        //创建匿名共享内存
                        val memoryFile = MemoryFile("memfile", contentBytes.size)
                        //写入字符数据
                        memoryFile.writeBytes(contentBytes, 0, 0, contentBytes.size)
                        //通过反射获得文件句柄
                        val method = MemoryFile::class.java.getDeclaredMethod("getFileDescriptor")
                        val fileDescriptor = method.invoke(memoryFile) as? FileDescriptor
                        val parcelFileDescriptor = ParcelFileDescriptor.dup(fileDescriptor)
                        //将文件句柄写到binder调用的返回值中
                        parcelFileDescriptor?.fileDescriptor?.let { reply?.writeFileDescriptor(it) }
                        true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }
                SHARE_BITMAP_CODE -> {
                    try {
                        //构造Bitmap
                        val createScaledBitmap = createBitmap()
                        log(TAG, "Server : share bitmap=$createScaledBitmap")

                        //将Bitmap转为byte数组
                        val contentBytes = createScaledBitmap.toByteArray()

                        //创建匿名共享内存
                        val memoryFile = MemoryFile("memfile", contentBytes.size)
                        //写入byte数据
                        memoryFile.writeBytes(contentBytes, 0, 0, contentBytes.size)
                        //通过反射获得文件句柄
                        val method = MemoryFile::class.java.getDeclaredMethod("getFileDescriptor")
                        val fileDescriptor = method.invoke(memoryFile) as? FileDescriptor
                        val parcelFileDescriptor = ParcelFileDescriptor.dup(fileDescriptor)
                        //将文件句柄写到binder调用的返回值中
                        parcelFileDescriptor?.fileDescriptor?.let {
                            reply?.writeFileDescriptor(it)
                        }
                        true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        false
                    }
                }
                else -> super.onTransact(code, data, reply, flags)
            }

        }

        private fun createBitmap(): Bitmap {
            val decodeResource = BitmapFactory.decodeResource(
                App.getAppContext().resources,
                R.mipmap.ic_launcher
            )
            return Bitmap.createScaledBitmap(decodeResource, 1024, 1024, false)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        log(TAG, "Server : onBind")
        return AshmemBinder()
    }

}