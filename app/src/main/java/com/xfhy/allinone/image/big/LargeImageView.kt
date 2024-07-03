package com.xfhy.allinone.image.big

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xfhy.library.ext.log

/**
 * @author : xfhy
 * Create time : 2021/3/16 6:13 AM
 * Description : 加载巨图的自定义View
 * 1. 提供一个设置图片的入口
 * 2. 重写onTouchEvent,在里面根据用户移动的手势,去更新显示区域的参数
 * 3. 每次更新区域参数后,调用invalidate,onDraw里面去regionDecoder.decodeRegion拿到Bitmap,去draw
 */
const val TAG = "large_image"

class LargeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mDecoder: BitmapRegionDecoder? = null
    private val mRect = Rect()

    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mWidth = 0
    private var mHeight = 0
    private val mOptions = BitmapFactory.Options()
    private var mDetector: MoveGestureDetector? = null

    init {
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565
        mDetector =
            MoveGestureDetector(context, object : MoveGestureDetector.SimpleMoveGestureDetector() {

                override fun onMove(detector: MoveGestureDetector): Boolean {
                    val moveX = detector.getMoveX().toInt()
                    val moveY = detector.getMoveY().toInt()

                    if (mImageWidth > width) {
                        mRect.offset(-moveX, 0)
                        checkWidth()
                        invalidate()
                    }

                    if (mImageHeight > height) {
                        mRect.offset(0, -moveY)
                        checkHeight()
                        invalidate()
                    }

                    return true
                }

            })
    }

    private fun checkHeight() {
        val rect = mRect
        val imageWidth = mImageWidth
        val imageHeight = mImageHeight

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight
            rect.top = imageHeight - height
        }
        if (rect.top < 0) {
            rect.top = 0
            rect.bottom = height
        }
    }

    private fun checkWidth() {
        val rect = mRect
        val imageWidth = mImageWidth
        val imageHeight = mImageHeight

        if (rect.right > imageWidth) {
            rect.right = imageWidth
            rect.left = imageWidth - width
        }
        if (rect.left < 0) {
            rect.left = 0
            rect.right = width
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        log(TAG, "onMeasure()")
        mWidth = measuredWidth
        mHeight = measuredHeight

        mRect.left = /*mImageWidth / 2 - mWidth / 2*/0
        mRect.top = /*mImageHeight / 2 - mHeight / 2*/0
        mRect.right = mRect.left + mWidth
        mRect.bottom = mRect.top + mHeight
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDetector?.onTouchEvent(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        log(TAG, "onLayout()")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        log(TAG, "onDraw()")
        val bitmap = mDecoder?.decodeRegion(mRect, mOptions)
        bitmap?.let {
            canvas?.drawBitmap(bitmap, 0f, 0f, null)
        }
    }

    fun setImageAssetPath(imageName: String) {
        var inputSteam = context.assets.open(imageName)

        mDecoder = BitmapRegionDecoder.newInstance(inputSteam, false)
        //这里不关的话,下面Bitmap的宽高测量出来永远为-1
        try {
            inputSteam.close()
        } catch (e: Exception) {
        }

        inputSteam = context.assets.open(imageName)
        val tempOptions = BitmapFactory.Options()
        tempOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputSteam, null, tempOptions)
        mImageWidth = tempOptions.outWidth
        mImageHeight = tempOptions.outHeight

        try {
            inputSteam.close()
        } catch (e: Exception) {
        }

        //requestLayout会导致View的onMeasure,onLayout,onDraw被调用
        requestLayout()

        //invalidate只会导致onDraw被调用
        invalidate()
    }

}