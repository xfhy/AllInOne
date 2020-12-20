package com.xfhy.allinone.ipc.aidl

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import com.xfhy.library.ext.log
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author : xfhy
 * Create time : 2020/12/15 6:19 AM
 * Description : aidl 服务端Service
 */
class AidlRemoteService : Service() {

    companion object {
        const val TAG = "xfhy_aidl"
    }

    private val mPersonList = CopyOnWriteArrayList<Person?>()
    private val mListenerList = RemoteCallbackList<IPersonChangeListener?>()

    private val mBinder: Binder = object : IPersonManager.Stub() {
        override fun getPersonList(): MutableList<Person?> = mPersonList

        override fun addPerson(person: Person?): Boolean {
            log(TAG, "服务端 addPerson() 当前线程 : ${Thread.currentThread().name}")
            log(TAG, "服务端 addPerson() person = $person")
            return mPersonList.add(person)
        }

        override fun addPersonIn(person: Person?) {
            log(TAG, "服务端 addPersonIn() person = $person")
            person?.name = "被addPersonIn修改"
        }

        override fun addPersonOut(person: Person?) {
            log(TAG, "服务端 addPersonOut() person = $person}")
            person?.name = "被addPersonOut修改"
        }

        override fun addPersonInout(person: Person?) {
            log(TAG, "服务端 addPersonInout() person = $person}")
            person?.name = "被addPersonInout修改"
        }

        override fun addPersonOneway(person: Person?) {
            mPersonList.add(person)
            Thread.sleep(2000)
        }

        override fun registerListener(listener: IPersonChangeListener?) {
            mListenerList.register(listener)
        }

        override fun unregisterListener(listener: IPersonChangeListener?) {
            mListenerList.unregister(listener)
        }
    }

    //死循环 每隔5秒添加一次person,通知观察者
    private val serviceWorker = Runnable {
        while (!Thread.currentThread().isInterrupted) {
            Thread.sleep(5000)
            val person = Person("name${Random().nextInt(10000)}")
            mPersonList.add(person)
            onDataChange(person)
        }
    }
    private val mServiceListenerThread = Thread(serviceWorker)

    override fun onBind(intent: Intent?): IBinder? {
        val check = checkCallingOrSelfPermission("com.xfhy.allinone.ipc.aidl.ACCESS_PERSON_SERVICE")
        if (check == PackageManager.PERMISSION_DENIED) {
            log(TAG,"没有权限")
            return null
        }
        log(TAG,"有权限")
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mPersonList.add(Person("Garen"))
        mPersonList.add(Person("Darius"))

        mServiceListenerThread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mServiceListenerThread.interrupt()
    }

    private fun onDataChange(person: Person?) {
        val callbackCount = mListenerList.beginBroadcast()
        for (i in 0 until callbackCount) {
            try {
                //这里try一下避免有异常时无法调用finishBroadcast()
                mListenerList.getBroadcastItem(i)?.onPersonDataChanged(person)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        mListenerList.finishBroadcast()
    }

}