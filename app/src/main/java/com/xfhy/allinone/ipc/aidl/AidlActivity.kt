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

    private var mRemoteServer: IPersonManager? = null
    private var mService: IBinder? = null
    private var mHasBind = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            this@AidlActivity.mService = service
            log(TAG, "onServiceConnected, thread = ${Thread.currentThread().name}")

            //给binder设置一个死亡代理
            service?.linkToDeath(mDeathRecipient, 0)

            //在onServiceConnected调用IPersonManager.Stub.asInterface获取接口类型的实例
            //通过这个实例调用服务端的服务
            mRemoteServer = IPersonManager.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            //主线程
            log(TAG, "onServiceDisconnected, thread = ${Thread.currentThread().name}")
        }
    }

    private val mPersonChangeListener = object : IPersonChangeListener.Stub() {
        override fun onPersonDataChanged(person: Person?) {
            log(TAG, "客户端 onPersonDataChanged() person = $person}")
        }
    }

    private val mDeathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            //子线程
            //监听 binder died
            log(TAG, "binder died, thread = ${Thread.currentThread().name}")
            //移除死亡通知
            mService?.unlinkToDeath(this, 0)
            mService = null
            //重新连接
            connectService()
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
        btnAddPersonIn.setOnClickListener {
            addPersonIn()
        }
        btnAddPersonOut.setOnClickListener {
            addPersonOut()
        }
        btnAddPersonInout.setOnClickListener {
            addPersonInout()
        }
        btnAddPersonOneway.setOnClickListener {
            addPersonOneway()
        }
        btnRegisterListener.setOnClickListener {
            registerListener()
        }
    }

    private fun connectService() {
        val intent = Intent()
        //action 和 package(app的包名)
        intent.action = "com.xfhy.aidl.Server.Action"
        intent.setPackage("com.xfhy.allinone")
        mHasBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        log(TAG, "bindService $mHasBind")
    }

    private fun addPerson() {
        //客户端调服务端方法时,需要捕获以下几个异常:
        //RemoteException 异常：
        //DeadObjectException 异常：连接中断时会抛出异常；
        //SecurityException 异常：客户端和服务端中定义的 AIDL 发生冲突时会抛出异常；
        try {
            val addPersonResult = mRemoteServer?.addPerson(Person("盖伦"))
            log(TAG, "addPerson result = $addPersonResult")
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: DeadObjectException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun getPerson() {
        val personList = mRemoteServer?.personList
        personList?.let {
            log(TAG, "personList ${it::class.java}")
        }
        //log(TAG, "person 列表 $personList")
    }

    private fun addPersonIn() {
        var person = Person("寒冰")
        log(TAG, "客户端 addPersonIn() 调用之前 person = $person}")
        mRemoteServer?.addPersonIn(person)
        log(TAG, "客户端 addPersonIn() 调用之后 person = $person}")
    }

    private fun addPersonOut() {
        var person = Person("蛮王")
        log(TAG, "客户端 addPersonOut() 调用之前 person = $person}")
        mRemoteServer?.addPersonOut(person)
        log(TAG, "客户端 addPersonOut() 调用之后 person = $person}")
    }

    private fun addPersonInout() {
        var person = Person("艾克")
        log(TAG, "客户端 addPersonInout() 调用之前 person = $person}")
        mRemoteServer?.addPersonInout(person)
        log(TAG, "客户端 addPersonInout() 调用之后 person = $person}")
    }

    /*
    //in 方式  服务端那边修改了,但是服务端这边不知道
    客户端 addPersonIn() 调用之前 person = Person(name=寒冰) hashcode = 142695478}
    服务端 addPersonIn() person = Person(name=寒冰) hashcode = 38642374
    客户端 addPersonIn() 调用之后 person = Person(name=寒冰) hashcode = 142695478}

    //out方式 客户端能感知服务端的修改,且客户端不能向服务端传数据
    //可以看到服务端是没有拿到客户端的数据的!
    客户端 addPersonOut() 调用之前 person = Person(name=蛮王) hashcode = 15787831}
    服务端 addPersonOut() person = Person(name=) hashcode = 231395975}
    客户端 addPersonOut() 调用之后 person = Person(name=被addPersonOut修改) hashcode = 15787831}

    //inout方式 客户端能感知服务端的修改
    客户端 addPersonInout() 调用之前 person = Person(name=艾克) hashcode = 143615140}
    服务端 addPersonInout() person = Person(name=艾克) hashcode = 116061620}
    客户端 addPersonInout() 调用之后 person = Person(name=被addPersonInout修改) hashcode = 143615140}
    * */

    private fun addPersonOneway() {
        log(TAG, "oneway开始时间: ${System.currentTimeMillis()}")
        mRemoteServer?.addPersonOneway(Person("oneway"))
        log(TAG, "oneway结束时间: ${System.currentTimeMillis()}")
        //oneway开始时间: 1608858291371
        //oneway结束时间: 1608858291372
    }

    private fun registerListener() {
        mRemoteServer?.registerListener(mPersonChangeListener)
    }

    private fun unregisterListener() {
        mRemoteServer?.asBinder()?.isBinderAlive?.let {
            mRemoteServer?.unregisterListener(mPersonChangeListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //最后记得unbindService
        unregisterListener()
        if (mHasBind) {
            unbindService(serviceConnection)
        }
    }

}
