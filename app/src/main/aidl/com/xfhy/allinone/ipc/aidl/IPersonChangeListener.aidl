package com.xfhy.allinone.ipc.aidl;
import com.xfhy.allinone.ipc.aidl.Person;

interface IPersonChangeListener {
    void onPersonDataChanged(in Person person);
}