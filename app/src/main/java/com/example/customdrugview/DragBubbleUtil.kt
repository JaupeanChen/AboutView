package com.example.customdrugview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.animation.doOnEnd
import com.example.customdrugview.ext.getBackgroundBitmap

/**
 * Author: ChenGuiPing
 * Date: 2022/9/8
 * Description:
 */
class DragBubbleUtil(private val view: View) {

    private val context by lazy { view.context }

    private val dragView by lazy {
        TempView(context)
    }

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val layoutParams by lazy {
        WindowManager.LayoutParams().apply {
            format = PixelFormat.TRANSLUCENT  //背景设为透明
        }
        //这里还要处理状态栏变黑色的问题
//        WindowManager.LayoutParams(
//
//        )
    }

    private val statusBarHeight by lazy {
        context.getStatusBarHeight()
    }

    private val cacheBitmap by lazy {
        view.getBackgroundBitmap()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun bind() {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //这里有个问题：第一次点击的时候，两个圆绘制位置不对
                    view.visibility = View.INVISIBLE  //控件主题和传给dragView的bitmap不同，所以得隐藏
                    windowManager.addView(dragView, layoutParams)

                    //初始化圆心坐标
                    val location = IntArray(2)
                    //获取当前窗口的绝对坐标(其实也就是左上角在window的位置？)
                    view.getLocationInWindow(location)
                    dragView.initCircleLocation(
                        location[0].toFloat() + view.width / 2,
                        location[1].toFloat() + view.height / 2 - statusBarHeight,
                        event.rawX,
                        event.rawY - statusBarHeight
                    )
                    //设置拖拽控件的bitmap
                    dragView.updateBitmapView(cacheBitmap, cacheBitmap.width.toFloat())
                }
                MotionEvent.ACTION_MOVE -> {
                    dragView.updateBigCircle(event.rawX, event.rawY - statusBarHeight)
                }
                MotionEvent.ACTION_UP -> {
                    if (dragView.isContains()) {
                        //回弹
                        dragView.bigCircleAnimator().run {
                            start()
                            doOnEnd {
                                //控件主体显示回来
                                view.visibility = View.VISIBLE
                                windowManager.removeView(dragView)
                                dragView.updateBitmapView(null, cacheBitmap.width.toFloat())
                            }
                        }
                    } else {
                        //爆炸
                    }


//                    //回弹
//                    dragView.bigCircleAnimator().start()
//
//                    windowManager.removeView(dragView)
//                    view.visibility = View.VISIBLE
//
//                    //清空拖拽控件的bitmap
//                    dragView.updateBitmapView(null, cacheBitmap.width.toFloat())

                }
            }
            return@setOnTouchListener true
        }
    }

    fun Context.getStatusBarHeight() = let {
        var height = 0
        val resourceId =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId)
        }
        height
    }

}