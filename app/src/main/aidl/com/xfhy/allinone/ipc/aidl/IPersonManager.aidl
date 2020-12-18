package com.xfhy.allinone.ipc.aidl;
import com.xfhy.allinone.ipc.aidl.Person;
import com.xfhy.allinone.ipc.aidl.IPersonChangeListener;

interface IPersonManager {
    List<Person> getPersonList();

    void addPersonIn(in Person person);

}
