package com.xfhy.allinone.ipc

import android.os.Bundle
import com.xfhy.allinone.R
import com.xfhy.allinone.ipc.aidl.AidlActivity
import com.xfhy.allinone.ipc.ashmem.AshmemActivity
import com.xfhy.allinone.ipc.binder.BinderActivity
import com.xfhy.allinone.ipc.messenger.MessengerActivity
import com.xfhy.library.basekit.activity.TitleBarActivity
import kotlinx.android.synthetic.main.activity_ipc_main.*
import org.jetbrains.anko.startActivity

class IpcMainActivity : TitleBarActivity() {

    override fun getThisTitle() = "IPC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipc_main)

        btnGoAidl.setOnClickListener {
            startActivity<AidlActivity>()
        }
        btnGoMessenger.setOnClickListener {
            startActivity<MessengerActivity>()
        }
        btnGoBinder.setOnClickListener {
            startActivity<BinderActivity>()
        }
        btnGoAshmem.setOnClickListener {
            startActivity<AshmemActivity>()
        }
    }
}
