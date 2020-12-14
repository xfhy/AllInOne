package com.xfhy.allinone.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.DeadObjectException
import android.os.IBinder
import android.os.RemoteException
import com.xfhy.allinone.R
import com.xfhy.library.basekit.activity.TitleBarActivity
import com.xfhy.library.ext.log
import kotlinx.android.synthetic.main.activity_aidl.*

/**
 * Aidl test
 */
class AidlActivity : TitleBarActivity() {

    companion object {
        const val TAG = "xfhy_aidl"
    }

    private var remoteServer: IPersonManager? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            log(TAG, "onServiceConnected")
            //在onServiceConnected调用IPersonManager.Stub.asInterface获取接口类型的实例
            //通过这个实例调用服务端的服务
            remoteServer = IPersonManager.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            log(TAG, "onServiceDisconnected")
        }
    }

    override fun getThisTitle(): CharSequence {
        return "AIDL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl)

        btnConnect.setOnClickListener {
            connectService()
        }
        btnGetPerson.setOnClickListener {
            getPerson()
        }
        btnAddPerson.setOnClickListener {
            addPerson()
        }
    }

    private fun connectService() {
        val intent = Intent()
        //action 和 package(app的包名)
        intent.action = "com.xfhy.aidl.Server.Action"
        intent.setPackage("com.xfhy.allinone")
        val bindServiceResult = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        log(TAG, "bindService $bindServiceResult")
    }

    private fun addPerson() {
        //客户端调服务端方法时,需要捕获以下几个异常:
        //RemoteException 异常：
        //DeadObjectException 异常：连接中断时会抛出异常；
        //SecurityException 异常：客户端和服务端中定义的 AIDL 发生冲突时会抛出异常；
        try {
            val addPersonResult = remoteServer?.addPerson(Person("盖伦"))
            log(TAG, "addPerson result = $addPersonResult")
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: DeadObjectException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun getPerson() {
        val personList = remoteServer?.personList
        log(TAG, "person 列表 $personList")
    }

    override fun onDestroy() {
        super.onDestroy()
        //最后记得unbindService
        unbindService(serviceConnection)
    }

}
