package com.xfhy.allinone.image.big

import android.content.Context
import android.view.MotionEvent

/**
 * @author : xfhy
 * Create time : 2021/3/16 7:05 AM
 * Description :
 */
abstract class BaseGestureDetector(protected val context: Context) {

    /**
     * 正在触摸
     */
    protected var mGestureInProgress = false
    protected var mPreMotionEvent: MotionEvent? = null
    protected var mCurrentMotionEvent: MotionEvent? = null

    fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mGestureInProgress) {
            handleStartProgressEvent(event)
        } else {
            handleInProgressEvent(event)
        }
        return true
    }

    protected abstract fun handleInProgressEvent(event: MotionEvent)

    protected abstract fun handleStartProgressEvent(event: MotionEvent)
    protected abstract fun updateStateByEvent(event: MotionEvent)

    protected fun resetState() {
        mPreMotionEvent?.recycle()
        mPreMotionEvent = null
        mCurrentMotionEvent?.recycle()
        mCurrentMotionEvent = null
        mGestureInProgress = false
    }

}