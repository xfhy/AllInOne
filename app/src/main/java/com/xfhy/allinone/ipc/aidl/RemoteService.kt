package com.xfhy.allinone.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.xfhy.library.ext.log
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author : xfhy
 * Create time : 2020/12/15 6:19 AM
 * Description : aidl 服务端Service
 */
class RemoteService : Service() {

    companion object {
        const val TAG = "xfhy_aidl"
    }

    private val mPersonList = CopyOnWriteArrayList<Person?>()

    private val mBinder: Binder = object : IPersonManager.Stub() {
        override fun getPersonList(): MutableList<Person?> = mPersonList

        override fun addPerson(person: Person?): Boolean {
            log(TAG, "服务端 addPerson() 当前线程 : ${Thread.currentThread().name}")
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
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mPersonList.add(Person("Garen"))
        mPersonList.add(Person("Darius"))
    }

}