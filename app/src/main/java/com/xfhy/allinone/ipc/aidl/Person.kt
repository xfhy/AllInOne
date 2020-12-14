package com.xfhy.allinone.ipc.aidl

import android.os.Parcel
import android.os.Parcelable

/**
 * @author : xfhy
 * Create time : 2020/12/15 6:15 AM
 * Description : 用来IPC传输的对象
 */
class Person(val name: String? = "") : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Person(name=$name)"
    }

}