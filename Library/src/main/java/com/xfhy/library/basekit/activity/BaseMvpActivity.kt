package com.xfhy.library.basekit.activity

import android.os.Bundle
import com.xfhy.library.basekit.presenter.IPresenter
import com.xfhy.library.basekit.view.IBaseView
import com.xfhy.library.widgets.LoadingDialog
import org.jetbrains.anko.toast

/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description MVP Activity基类
 */
abstract class BaseMvpActivity<P : IPresenter<in IBaseView>> : BaseActivity(), IBaseView {
    protected var mPresenter: P? = null
    private var mFlagDestroy = false
    private val mDialog by lazy { LoadingDialog.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFlagDestroy = false
        initPresenter()
        mPresenter?.setView(this)
    }

    abstract fun initPresenter()

    /**
     * 显示加载中对话框
     */
    override fun showLoading() {
        mDialog.show()
    }

    /**
     * 隐藏加载中对话框
     */
    override fun hideLoading() {
        mDialog.hide()
    }

    override fun showEmptyView() {
    }

    override fun showOffline() {
    }

    override fun showContent() {
    }

    override fun showErrorMsg(errorMsg: String) {
        toast(errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFlagDestroy = true
        hideLoading()
    }

    override fun isViewDestroy(): Boolean = mFlagDestroy

}