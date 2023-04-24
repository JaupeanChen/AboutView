package com.example.customdrugview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ScrollView
import kotlin.math.abs

/**
 * Author: ChenGuiPing
 * Date: 2023/4/19
 * Description:
 */
class SimpleScrollView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : ScrollView(context, attributeSet, def) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.actionMasked == MotionEvent.ACTION_DOWN) {
            //因为Parent ScrollView是滑动（MOVE）的时候才去拦截，
            //所以我们提前一步去请求不要拦截，获取事件
            parent.requestDisallowInterceptTouchEvent(true)
//            return true
        }
        if (ev?.actionMasked == MotionEvent.ACTION_MOVE) {
            //如果我们（child view）滑到顶或者滑到底，就把滑动事件还给父类
            val offset = ev.y.toInt() - getLastMotionY()
//            Log.d("Scroll", "mLastMotionY: ${getLastMotionY()}")
//            Log.d("Scroll", "offset: $offset")

            val viewConfiguration = ViewConfiguration.get(context)
            val touchSlop = viewConfiguration.scaledTouchSlop
//            Log.d("Scroll", "touchSlop: $touchSlop")

            Log.d("Scroll", "scroll: $scrollY")
            //假定mTouchSlop为24
            if (abs(offset) >= touchSlop) {
                //达到滑动界定, offset > 0 下滑，offset < 0 上划
                if ((offset > 0 && isScrollTop()) || (offset < 0 && isScrollBottom())) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }

        return super.onTouchEvent(ev)
    }

    //连续滑动？
    override fun scrollBy(x: Int, y: Int) {
        if ((y > 0 && isScrollTop()) || (y < 0 && isScrollBottom())) {
            (parent as View).scrollBy(x, y)
            return
        }
        super.scrollBy(x, y)
    }

    private fun getTouchSlop(): Int {
        val clazz = Class.forName("android.view.View")
        //TODO 反射不到mTouchSlop。。
//        val field = clazz.getDeclaredField("mTouchSlop")
        return 0
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun getLastMotionY(): Int {
        val clazz = ScrollView::class.java
        val field = clazz.getDeclaredField("mLastMotionY")
        field.isAccessible = true
        return field.get(this) as Int
    }

    private fun isScrollTop(): Boolean {
//        Log.d("Scroll", "scroll: $scrollY")
        return scrollY == 0
    }

    private fun isScrollBottom(): Boolean {
        return (scrollY + height - paddingTop - paddingBottom) == getChildAt(0).height
    }

}