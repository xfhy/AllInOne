package com.xfhy.allinone.ipc.aidl;
import com.xfhy.allinone.ipc.aidl.Person;

interface IPersonManager {
    List<Person> getPersonList();

    boolean addPerson(in Person person);

    void addPersonIn(in Person person);
    void addPersonOut(out Person person);
    void addPersonInout(inout Person person);

    oneway void addPersonOneway(in Person person);

}
