package com.xfhy.library.basekit.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.xfhy.library.basekit.view.IBaseView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author xfhy
 * create at 2017/12/17 9:23
 * description：基于RxJava2的Presenter封装,控制订阅的生命周期
 */
open class BasePresenter<T : IBaseView> : IPresenter<T>, LifecycleObserver, InvocationHandler {
    private var mView: T? = null
    protected var mViewProxy: T? = null

    /*
    * 如果有多个Disposable , RxJava中已经内置了一个容器CompositeDisposable,
    * 每当我们得到一个Disposable时就调用CompositeDisposable.add()将它添加到容器中, 在退出的时候, 调用CompositeDisposable
    * .clear() 即可切断所有的水管.
    * */
    protected var mCompositeDisposable: CompositeDisposable? = null

    override fun setView(view: T) {
        mView = view

        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        //注册观察者
        if (mView != null && mView is LifecycleOwner) {
            (mView as LifecycleOwner).lifecycle.addObserver(this)
        }

        //找到View的接口 是继承BaseView的
        val interfaces = view::class.java.interfaces
        var findIt = false
        for (anInterface in interfaces) {
            val anInterfaceInterfaces = anInterface.interfaces
            for (anInterfaceInterface in anInterfaceInterfaces) {
                if (IBaseView::class.java == anInterfaceInterface) {
                    mViewProxy = Proxy.newProxyInstance(
                        anInterface.classLoader,
                        arrayOf(anInterface),
                        this
                    ) as T
                    findIt = true
                    break
                }
            }
            if (findIt) {
                break
            }
        }
        if (mViewProxy == null) {
            mViewProxy = Proxy.newProxyInstance(
                IBaseView::class.java.classLoader,
                arrayOf<Class<*>>(IBaseView::class.java),
                this
            ) as T
        }
    }

    /**
     * 添加到容器中,方便控制
     *
     * @param disposable 用于解除订阅
     */
    protected fun addSubscribe(disposable: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable?.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        mView = null
        if (mCompositeDisposable != null) {
            mCompositeDisposable?.clear()
        }
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        if (mView == null || method == null) {
            return null
        }
        if (mView?.isViewDestroy() == true) {
            return null
        }
        try {
            return method.invoke(mView, args)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.targetException.printStackTrace()
        }

        return null
    }


}
