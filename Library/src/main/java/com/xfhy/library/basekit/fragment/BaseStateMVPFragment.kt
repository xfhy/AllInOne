package com.xfhy.library.basekit.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.xfhy.library.R

import com.xfhy.library.basekit.presenter.BasePresenter
import com.xfhy.library.basekit.view.IBaseView
import com.xfhy.library.utils.DevicesUtils
import com.xfhy.library.utils.SnackbarUtil
import com.xfhy.library.widgets.StatefulLayout

/**
 * @author xfhy
 * create at 2018年3月10日16:45:18
 * description：所有需要刷新和请求网络的fragment可以继承自该fragment,已实现基本的展示空布局,刷新布局,显示错误信息等
 * MVP架构,布局中必须是有StatefulLayout,并且子类必须去fbc该id
 */
abstract class BaseStateMVPFragment<P : BasePresenter<in IBaseView>> : BaseFragment(), IBaseView {

    protected var mPresenter: P? = null
    private var mFlagDestroy = false

    var mStateView: StatefulLayout? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        initPresenter()
        mPresenter?.setView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFlagDestroy = false
    }

    override fun onDestroy() {
        super.onDestroy()
        mFlagDestroy = true
        mPresenter = null
    }

    /**
     * 初始化presenter
     */
    abstract fun initPresenter()

    override fun showErrorMsg(errorMsg: String) {
        hideLoading()
        mRootView?.let {
            SnackbarUtil.showBarLongTime(it, errorMsg, SnackbarUtil.ALERT)
        }
    }

    override fun showEmptyView() {
        closeRefresh()
        mStateView?.showEmpty(R.string.stfLayoutEmptyMessage, R.string.stfLayoutButtonRetry)
    }

    /**
     * 停止刷新
     */
    abstract fun closeRefresh()

    override fun showOffline() {
        closeRefresh()
        mStateView?.showOffline(
            R.string.stfLayoutOfflineMessage,
            R.string.stfLayoutButtonSetting,
            View.OnClickListener {
                //未联网  跳转到设置界面
                DevicesUtils.goSetting(activity)
            })
    }


    override fun showContent() {
        closeRefresh()
        mStateView?.showContent()
    }

    override fun showLoading() {
        mStateView?.showLoading()
    }

    override fun hideLoading() {
        mStateView?.showContent()
    }

    override fun onBackPress(): Boolean {
        return false
    }

    override fun isViewDestroy(): Boolean = mFlagDestroy
}
