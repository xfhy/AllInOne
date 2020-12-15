package com.xfhy.allinone.ipc.aidl;
import com.xfhy.allinone.ipc.aidl.Person;

interface IPersonManager {
    List<Person> getPersonList();

    boolean addPerson(in Person person);
}
