package com.xfhy.allinone.image.big

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * @author : xfhy
 * Create time : 2021/3/16 6:13 AM
 * Description : 加载巨图的自定义View
 * 1. 提供一个设置图片的入口
 * 2. 重写onTouchEvent,在里面根据用户移动的手势,去更新显示区域的参数
 * 3. 每次更新区域参数后,调用invalidate,onDraw里面去regionDecoder.decodeRegion拿到Bitmap,去draw
 */
class LargeImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mDecoder: BitmapRegionDecoder? = null
    private val mRect = Rect()

    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mWidth = 0
    private var mHeight = 0
    private val mOptions = BitmapFactory.Options()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight

        mRect.left = mImageWidth / 2 - mWidth / 2
        mRect.top = mImageHeight / 2 - mHeight / 2
        mRect.right = mRect.left + mWidth
        mRect.bottom = mRect.top + mHeight
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var startX = 0f
        var startY = 0f
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                var endX = event.x
                var endY = event.y
                mRect.left = mRect.left+endX.toInt()-startX.toInt()
                mRect.top = mRect.top+endY.toInt()-startY.toInt()
                mRect.right = mRect.left + mWidth
                mRect.bottom = mRect.top + mHeight
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
            else -> {
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bitmap = mDecoder?.decodeRegion(mRect, mOptions)
        bitmap?.let {
            canvas?.drawBitmap(bitmap, 0f, 0f, mPaint)
        }
    }

    fun setImageAssetPath(imageName: String) {
        val inputSteam = context.assets.open(imageName)
        /*
        *  val bitmapRegionDecoder = BitmapRegionDecoder.newInstance(assets.open("tobias.jpg"), false)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap = bitmapRegionDecoder.decodeRegion(Rect(0, 0, 200, 200), options)
        ivShowImage.setImageBitmap(bitmap)
        * */
        //val bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputSteam, false)

        mDecoder = BitmapRegionDecoder.newInstance(inputSteam,false)
        val tempOptions = BitmapFactory.Options()
        tempOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputSteam,null,tempOptions)
        mImageWidth = tempOptions.outWidth
        mImageHeight = tempOptions.outHeight

        /*mOptions.inJustDecodeBounds = true
        val bitmap = BitmapFactory.decodeStream(inputSteam, null, mOptions)
        mImageWidth = bitmap?.width ?: 0
        mImageHeight = bitmap?.height ?: 0

        mDecoder = BitmapRegionDecoder.newInstance(inputSteam, false)
        mOptions.inJustDecodeBounds = false
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565*/

        requestLayout()
        invalidate()
    }

}