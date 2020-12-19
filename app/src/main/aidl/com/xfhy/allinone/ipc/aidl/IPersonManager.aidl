package com.xfhy.allinone.ipc.aidl;
import com.xfhy.allinone.ipc.aidl.Person;
import com.xfhy.allinone.ipc.aidl.IPersonChangeListener;

interface IPersonManager {
    List<Person> getPersonList();

    boolean addPerson(in Person person);

    void addPersonIn(in Person person);
    void addPersonOut(out Person person);
    void addPersonInout(inout Person person);

    oneway void addPersonOneway(in Person person);

    void registerListener(IPersonChangeListener listener);
    void unregisterListener(IPersonChangeListener listener);

}
