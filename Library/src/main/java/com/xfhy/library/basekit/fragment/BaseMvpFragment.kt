package com.xfhy.library.basekit.fragment

import android.content.Context
import android.os.Bundle
import com.xfhy.library.basekit.presenter.IPresenter
import com.xfhy.library.basekit.view.IBaseView
import com.xfhy.library.ext.toast
import com.xfhy.library.widgets.LoadingDialog

/**
 * @author xfhy
 * time create at 2018/1/27 9:09
 * description 需要使用MVP的fragment的基类
 */
abstract class BaseMvpFragment<T : IPresenter<in IBaseView>> : BaseFragment(), IBaseView {
    protected var mPresenter: T? = null
    private var mFlagDestroy = false
    private val mDialog by lazy {
        activity?.let { LoadingDialog.create(it) }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        initPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFlagDestroy = false
        initPresenter()
        mPresenter?.setView(this)
    }

    override fun showLoading() {
        mDialog?.show()
    }

    override fun hideLoading() {
        if (mDialog?.isShowing != false) {
            mDialog?.hide()
        }
    }

    override fun showErrorMsg(errorMsg: String) {
        toast(errorMsg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter = null
        mFlagDestroy = true
    }

    override fun showContent() {
    }

    override fun showEmptyView() {
    }

    override fun showOffline() {
    }

    override fun isViewDestroy(): Boolean = mFlagDestroy

    /**
     * 初始化presenter
     */
    abstract fun initPresenter()

}