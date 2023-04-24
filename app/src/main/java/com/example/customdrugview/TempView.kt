package com.example.customdrugview

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PointFEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.graphics.minus
import com.example.customdrugview.ext.contains
import com.example.customdrugview.ext.dp
import com.example.customdrugview.ext.getBitMap
import kotlin.math.*

/**
 * Instructs the Kotlin compiler to generate overloads for this function that substitute default parameter values.
 * @JvmOverloads 注解会指示 Kotlin 编译器为此函数生成替换默认参数值的重载
 */
class TempView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    companion object {
        private const val bigRadius = 70f

        private const val smallRadius = 70f

        private const val supportRadius = 400f
    }

    private val paint = Paint()
    private var bigMove = false

    //大圆初始位置
    //此时通过属性动画来改变bigPointF肯定是不可取的,因为他是懒加载
//    private val bigPointF by lazy {
//        PointF(width / 2f + 300, height / 2f)
//    }
    //则应改为：
    //注意：这里bigPointF不能再设置为私有，否则bigCircleAnimator()方法将访问不到
    //应该是通过反射去设置属性
    var bigPointF = PointF(0f, 0f)
        set(value) {
            field = value
            invalidate()
        }

    //大球回弹动画
    fun bigCircleAnimator(): Animator {
        return ObjectAnimator.ofObject(
            this,
            "bigPointF",
            PointFEvaluator(),
            PointF(smallPointF.x, smallPointF.y)
        ).apply {
            duration = 400
            //设置回弹插值器
            interpolator = OvershootInterpolator(3f)
        }
    }

    //小圆初始位置
    private val smallPointF by lazy {
        PointF(width / 2f, height / 2f)
    }

    //爆炸下标
    //其实也就是爆炸效果的帧
    var explodeIndex = -1
        set(value) {
            field = value
            invalidate()
        }

    //通过属性动画修改爆炸下标，最后一帧的时候回到-1
    private val explodeAnimator by lazy {
        ObjectAnimator.ofInt(this, "explodeIndex", 19, -1).apply {
            duration = 1000
        }
    }

    private val explodeImages by lazy {
        val list = arrayListOf<Bitmap>()
        val width = (bigRadius * 2).toInt()
        list.add(getBitMap(R.mipmap.explode_0, width))
        list.add(getBitMap(R.mipmap.explode_1, width))
        list.add(getBitMap(R.mipmap.explode_2, width))
        list.add(getBitMap(R.mipmap.explode_3, width))
        list.add(getBitMap(R.mipmap.explode_4, width))
        list.add(getBitMap(R.mipmap.explode_5, width))
        list.add(getBitMap(R.mipmap.explode_6, width))
        list.add(getBitMap(R.mipmap.explode_7, width))
        list.add(getBitMap(R.mipmap.explode_8, width))
        list.add(getBitMap(R.mipmap.explode_9, width))
        list.add(getBitMap(R.mipmap.explode_10, width))
        list.add(getBitMap(R.mipmap.explode_11, width))
        list.add(getBitMap(R.mipmap.explode_12, width))
        list.add(getBitMap(R.mipmap.explode_13, width))
        list.add(getBitMap(R.mipmap.explode_14, width))
        list.add(getBitMap(R.mipmap.explode_15, width))
        list.add(getBitMap(R.mipmap.explode_16, width))
        list.add(getBitMap(R.mipmap.explode_17, width))
        list.add(getBitMap(R.mipmap.explode_18, width))
        list.add(getBitMap(R.mipmap.explode_19, width))
        list
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bigPointF.x = width / 2f
        bigPointF.y = height / 2f
    }

    fun initCircleLocation(smallX: Float, smallY: Float, bigX: Float, bigY: Float) {
        smallPointF.x = smallX
        smallPointF.y = smallY
        bigPointF.x = bigX
        bigPointF.y = bigY
    }

    fun updateBigCircle(x: Float, y: Float) {
        bigPointF.x = x
        bigPointF.y = y
        invalidate()
    }

    private var bitmapView: Bitmap? = null
    private var bitmapViewWidth = 0f

    fun updateBitmapView(bitmap: Bitmap?, width: Float) {
        this.bitmapView = bitmap
        this.bitmapViewWidth = width
        invalidate()
    }

//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                //先判断点击点是否在大圆内，如果是再进行移动
//                val locX = event.x
//                val locY = event.y
//                val minX = bigPointF.x - bigRadius
//                val maxX = bigPointF.x + bigRadius
//                val minY = bigPointF.y - bigRadius
//                val maxY = bigPointF.y + bigRadius
//                val isContainsX = locX in minX..maxX
//                val isContainsY = locY in minY..maxY
//                bigMove = isContainsX && isContainsY
//
//                if (!bigPointF.contains(smallPointF, supportRadius)) {
//                    //当大圆消失时，重置
//                    bigPointF.x = width / 2f
//                    bigPointF.y = height / 2f
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                if (bigMove) {
//                    bigPointF.x = event.x
//                    bigPointF.y = event.y
//                }
//            }
//            MotionEvent.ACTION_UP -> {
//                if (bigPointF.contains(smallPointF, supportRadius)) {
//                    //回弹
//                    bigCircleAnimator().start()
//                } else {
//                    //绘制爆炸效果
//                    explodeAnimator.start()
//                    //爆炸结束，重置大圆坐标
//                    explodeAnimator.doOnEnd {
//                        bigPointF.x = width / 2f
//                        bigPointF.y = height / 2f
//                    }
//                }
//            }
//        }
//        invalidate()
//        //把事件消费掉
//        return true
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //重绘时重置颜色
        paint.color = Color.CYAN

        val d = circlesDistance()
        var ratio = d / supportRadius
        if (ratio > 0.6f) {
            ratio = 0.6f
        }
        //绘制小圆
        val updateSmallRadius = smallRadius - smallRadius * ratio
        canvas.drawCircle(smallPointF.x, smallPointF.y, updateSmallRadius, paint)

        //绘制大圆
        if (bigPointF.contains(smallPointF, supportRadius)) {
            //当大圆在辅助圆内再绘制
            canvas.drawCircle(bigPointF.x, bigPointF.y, bigRadius, paint)

            //绘制贝塞尔曲线
            drawBezier(canvas, updateSmallRadius, bigRadius)
        }

        //绘制被拖拽控件的bitmap
        bitmapView?.let {
            canvas.drawBitmap(it, bigPointF.x - it.width / 2f, bigPointF.y - it.height / 2f, paint)
        }

        //绘制爆炸效果
        if (explodeIndex != -1) {
            //圆和bitmap的绘制坐标系不同，圆在中心，bitmap在左上角
            canvas.drawBitmap(
                explodeImages[explodeIndex],
                bigPointF.x - bigRadius,
                bigPointF.y - bigRadius,
                paint
            )
        }

        //绘制辅助圆
        paint.color = Color.argb(20, 255, 0, 0)
        canvas.drawCircle(smallPointF.x, smallPointF.y, supportRadius, paint)
    }

    private fun circlesDistance(): Float {
        //利用ktx中自带的运算符重载函数
        val distance = bigPointF - smallPointF
        return sqrt(distance.x.toDouble().pow(2.0) + (distance.y.toDouble().pow(2.0)))
            .toFloat()
    }

    //抛给外部用，判断拖拽控件中心是否在辅助圆内
    fun isContains(): Boolean {
        return smallPointF.contains(bigPointF, supportRadius)
    }

//    fun PointF.contains(dot: PointF, radius: Float): Boolean {
//        val minX = this.x - radius
//        val maxX = this.x + radius
//        val minY = this.y - radius
//        val maxY = this.y + radius
//        val isContainsX = dot.x in minX..maxX
//        val isContainsY = dot.y in minY..maxY
//        return isContainsX && isContainsY
//    }

    private fun PointF.isContains(b: PointF, padding: Float = 0f): Boolean {
        val isX = this.x <= b.x + padding && this.x >= b.x - padding
        val isY = this.y <= b.y + padding && this.y >= b.y - padding
        return isX && isY
    }

    //dp2px
    fun Number.dp(): Int {
        val f = toFloat()
        val scale: Float = Resources.getSystem().displayMetrics.density
        return (f * scale + 0.5f).toInt()
    }

    private fun drawBezier(canvas: Canvas, smallRadius: Float, bigRadius: Float) {
//        val pointA = PointF()
//        var bc = 0f
//        var ac = 0f
        //也就是求出ac和bc即可推算出A点
//        pointA.x = smallPointF.x + bc
//        pointA.y = smallPointF.y - ac

        val current = bigPointF - smallPointF
        //FD,BF长度
        //这里为什么要转Double？
        val FD = current.x.toDouble()
        val BF = current.y.toDouble()
        //角BDF
        val BDF = atan(BF / FD)

        //计算出P1点坐标
//        bc = (smallRadius * sin(BDF)).toFloat()
//        ac = (smallRadius * cos(BDF)).toFloat()
//        pointA.x = smallPointF.x + bc
//        pointA.y = smallPointF.y - ac

        val p1X = smallPointF.x + smallRadius * sin(BDF)
        val p1Y = smallPointF.y - smallRadius * cos(BDF)

        //计算出P2点坐标
        val p2X = bigPointF.x + bigRadius * sin(BDF)
        val p2Y = bigPointF.y - bigRadius * cos(BDF)

        //计算出P3点坐标
        val p3X = smallPointF.x - smallRadius * sin(BDF)
        val p3Y = smallPointF.y + smallRadius * cos(BDF)

        //计算出P4点坐标
        val p4X = bigPointF.x - bigRadius * sin(BDF)
        val p4Y = bigPointF.y + bigRadius * cos(BDF)

        //控制点
        val controlPointX = current.x / 2 + smallPointF.x
        val controlPointY = current.y / 2 + smallPointF.y

        val path = Path()
        //移动到P1位置
        path.moveTo(p1X.toFloat(), p1Y.toFloat())
        //绘制贝塞尔
        path.quadTo(controlPointX, controlPointY, p2X.toFloat(), p2Y.toFloat())

        //连接到P4
        path.lineTo(p4X.toFloat(), p4Y.toFloat())
        //绘制贝塞尔
        //这里因为第二个参数写错成controlPointX，排查了一下午没排查出来！细心细心！！
        path.quadTo(controlPointX, controlPointY, p3X.toFloat(), p3Y.toFloat())
        //连接到P1
        path.close()

        //绘制
        canvas.drawPath(path, paint)
    }

}