package com.xfhy.allinone.ipc.messenger;

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /var/tmp/compile6972708932982633239/combined/android/os/IMessenger.aidl
 */

//package android.os;

/**
 * 根据IMessenger.aidl生成的文件大概长这样
 */
public interface IMessenger extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements IMessenger {
        private static final java.lang.String DESCRIPTOR = "android.os.IMessenger";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an android.os.IMessenger interface,
         * generating a proxy if needed.
         */
        public static IMessenger asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof IMessenger))) {
                return ((IMessenger) iin);
            }
            return new IMessenger.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_send: {
                    data.enforceInterface(descriptor);
                    android.os.Message _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = android.os.Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.send(_arg0);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements IMessenger {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void send(android.os.Message msg) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((msg != null)) {
                        _data.writeInt(1);
                        msg.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_send, _data, null, android.os.IBinder.FLAG_ONEWAY);
                } finally {
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_send = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }

    public void send(android.os.Message msg) throws android.os.RemoteException;
}
