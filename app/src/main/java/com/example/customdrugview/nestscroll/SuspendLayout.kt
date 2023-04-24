package com.example.customdrugview.nestscroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.math.MathUtils
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

/**
 * Author: ChenGuiPing
 * Date: 2023/4/23
 * Description:
 */
class SuspendLayout(
    context: Context?,
    attributeSet: AttributeSet? = null,
) : LinearLayout(context, attributeSet), NestedScrollingParent3 {
    private val tag = "NestScroll"

    private var headerChildHeight = 0
    private val scrollingParentHelper = NestedScrollingParentHelper(this)

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        Log.d(tag, "onStartNestedScroll")
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

//    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
//        return onStartNestedScroll(child!!, target!!, nestedScrollAxes, ViewCompat.TYPE_TOUCH)
//    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.d(tag, "onNestedScrollAccepted")
        scrollingParentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.d(tag, "onStopNestedScroll")
        scrollingParentHelper.onStopNestedScroll(target, type)
    }

    //第一次滑动消费回调
    //为什么嵌套滑动机制要先做PreScroll的意义在这里就体现出来了，我们希望父类先滑动，接着子类再滑动
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.d(tag, "onNestedPreScroll")
        //上划的时候我们（父类）先消费
        if (dy > 0) {
            //int deltaY = mLastMotionY - y;
            //dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset, ViewCompat.TYPE_TOUCH);
            //因为这里我们要先消费的上滑，而分发出来的deltaY是mLastMotionY - y，所以上滑的时候是正值
            Log.d(tag, "dy: $dy")
            scrollUp(dy, consumed)
        }
    }

    //第二次滑动消费回调
    //也就是子类滑动完，传递给父类滑动的情况，这边对应的就是下滑的情况，
    //下滑时，我们希望子view先滑完，剩下的再给父view消费
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.d(tag, "onNestedScroll in NestedScrollingParent3")
        Log.d(tag, "dyUnconsumed: $dyUnconsumed")
        nestScrollInternal(dyUnconsumed, consumed)
    }

    //NestedScrollParent2
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.d(tag, "onNestedScroll in NestedScrollingParent2")
        nestScrollInternal(dyUnconsumed, null)
    }

    //
    private fun nestScrollInternal(dyUnconsumed: Int, consumed: IntArray?) {
        //TODO 这边下滑为什么要判断<0？
        //int deltaY = mLastMotionY - y;
        //final int unconsumedY = deltaY - scrolledDeltaY;
        //但这里final int scrolledDeltaY = getScrollY() - oldY; scrolledDeltaY为正值，unconsumedY不是会变成更大的负值
        //unconsumedY不是会变成更大的负值，有点说不通
        Log.d(tag, "dyUnconsumed: $dyUnconsumed")
        //分发的时候用本次滑动的距离deltaY去减掉子view消费消费掉的距离scrolledDeltaY
        if (dyUnconsumed < 0) scrollDown(dyUnconsumed, consumed)
    }

    override fun scrollTo(x: Int, y: Int) {
        //限制自己的滑动范围，最多只能滑动header（第一个view）的高度，
        //这样一来也就达到了第二个view悬停的效果
        MathUtils.clamp(y, 0, headerChildHeight)
        super.scrollTo(x, y)
    }

    //测量自己第一个子view的高度，呜呜，自定义view三部曲的知识点又得重拾一下了T_T
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount > 0) {
            val headerView = getChildAt(0)
            measureChildWithMargins(headerView, widthMeasureSpec, 0, MeasureSpec.UNSPECIFIED, 0)
            headerChildHeight = headerView.measuredHeight
            Log.d(tag, "headerChildHeight: $headerChildHeight")
            super.onMeasure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(heightMeasureSpec) + headerChildHeight,
                    MeasureSpec.EXACTLY
                )
            )
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    //--------------mine start --------------------

    private fun scrollUp(dy: Int, consumed: IntArray) {
        val oldScroll = scrollY
        scrollBy(0, dy)
        consumed[1] = scrollY - oldScroll
    }

    private fun scrollDown(dyUnconsumed: Int, consumed: IntArray?) {
        val oldScroll = scrollY
        if (dyUnconsumed > 0) {
            scrollBy(0, dyUnconsumed)
        }
        val myConsume = scrollY - oldScroll
        if (consumed != null) {
            consumed[1] += myConsume
        }
    }

    //--------------mine end ----------------------

}