package com.xfhy.allinone.ipc.aidl;
import com.xfhy.allinone.ipc.aidl.Person;

interface IPersonManager {
    List<Person> getPersonList();

    //in: 从客户端流向服务端
    boolean addPerson(in Person person);
}
