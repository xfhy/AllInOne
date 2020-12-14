package com.xfhy.allinone.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * @author : xfhy
 * Create time : 2020/12/15 6:19 AM
 * Description : aidl 服务端Service
 */
class RemoteService : Service() {

    private val mPersonList = mutableListOf<Person?>()

    private val mBinder: Binder = object : IPersonManager.Stub() {
        override fun getPersonList(): MutableList<Person?> = mPersonList

        override fun addPerson(person: Person?): Boolean {
            return mPersonList.add(person)
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