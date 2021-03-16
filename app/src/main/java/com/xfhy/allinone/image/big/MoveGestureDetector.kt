package com.xfhy.allinone.image.big

import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent

/**
 * @author : xfhy
 * Create time : 2021/3/16 7:04 AM
 * Description : 监听用户move手势
 */
class MoveGestureDetector(context: Context, val mListener: OnMoveGestureListener) :
    BaseGestureDetector(context) {

    private var mCurrentPointer: PointF? = null
    private var mPrePointer: PointF? = null

    //仅为了减少创建内存
    private val mDeltaPointer = PointF()

    //记录最终结果,并返回
    private val mExtenalPointer = PointF()

    override fun handleInProgressEvent(event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_CANCEL -> {
            }
            MotionEvent.ACTION_MOVE -> {
                updateStateByEvent(event)
                val update = mListener.onMove(this)
                if (update) {
                    mPreMotionEvent?.recycle()
                    mPreMotionEvent = MotionEvent.obtain(event)
                }
            }
            MotionEvent.ACTION_UP -> {
                mListener.onMoveEnd(this)
                resetState()
            }
            else -> {
            }
        }
    }

    override fun handleStartProgressEvent(event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                //防止没有接收到CANCEL or UP ,保险起见
                resetState()

                mPreMotionEvent = MotionEvent.obtain(event)
                updateStateByEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                //开始滑动
                mGestureInProgress = mListener.onMoveBegin(this)
            }
            else -> {
            }
        }
    }

    override fun updateStateByEvent(event: MotionEvent) {
        val prev = mPreMotionEvent
        mPrePointer = caculateFocalPointer(prev)
        mCurrentPointer = caculateFocalPointer(event)

        //多点触控
        val skipThisMoveEvent = prev?.pointerCount != event.pointerCount
        mExtenalPointer.x =
            if (skipThisMoveEvent) 0f else (mCurrentPointer?.x ?: 0f) - (mPrePointer?.x ?: 0f)
        mExtenalPointer.y =
            if (skipThisMoveEvent) 0f else (mCurrentPointer?.y ?: 0f) - (mPrePointer?.y ?: 0f)
    }

    /**
     * 根据event计算多指中心点
     */
    private fun caculateFocalPointer(event: MotionEvent?): PointF? {
        val pointerCount = event?.pointerCount ?: 1
        var x = 0f
        var y = 0f
        for (i in 0..pointerCount) {
            x += (event?.x ?: 0f)
            y += (event?.y ?: 0f)
        }
        x /= pointerCount
        y /= pointerCount
        return PointF(x, y)
    }

    fun getMoveX(): Float = mExtenalPointer.x

    fun getMoveY() = mExtenalPointer.y

    interface OnMoveGestureListener {
        fun onMoveBegin(detector: MoveGestureDetector): Boolean
        fun onMove(detector: MoveGestureDetector): Boolean
        fun onMoveEnd(detector: MoveGestureDetector)
    }

    open class SimpleMoveGestureDetector : OnMoveGestureListener {
        override fun onMoveBegin(detector: MoveGestureDetector) = true

        override fun onMove(detector: MoveGestureDetector) = false

        override fun onMoveEnd(detector: MoveGestureDetector) {
        }
    }

}