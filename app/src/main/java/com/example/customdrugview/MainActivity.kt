package com.example.customdrugview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import androidx.core.view.children
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.btn)
//        DragBubbleUtil(button).bind()
        button.setOnClickListener {
            Log.d("ClickProxy", "我被点击啦！")
        }
        window.decorView.post {
            HookViewClickUtil.hookView(button)
        }

//        MainScope().launch {
//            flow {
//                (0..5).forEach {
//                    delay(1000)
//                    Log.d("Flow", "发射数据：$it")
//                    emit(it)
//                }
//            }.collect {
//                // 收集/接收数据，即消费数据
//                Log.d("Flow", "接收数据：$it")
//            }
//
//            flow<Int> {
//                emit(1)
//            }.collect {
//
//            }
//
//        }

    }

    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    fun changeClickListenerByReflect(view: View) {
//        val clazz = View::class.java
//        val field = clazz.getDeclaredField("mListenerInfo")
//        field.isAccessible = true
//        val listenerInfo = field.get(view) //获取到mListenerInfo实例
//        //TODO 为什么这里反射不到View$ListenerInfo类？？
//        val infoClazz = Class.forName("android.view.View$ListenerInfo")
//        val field1 = infoClazz.getDeclaredField("mOnClickListener")
//        field1.isAccessible = true
//        val mOnClickListener =
//            field1.get(listenerInfo) as View.OnClickListener //获取到mOnClickListener实例
//        val clickProxy = ClickProxy(mOnClickListener)
//        field1.set(listenerInfo, clickProxy)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val decorView = window.decorView as ViewGroup
            val childCount = decorView.childCount
            Log.d("DecorView.childCount", childCount.toString())
            val children = decorView.children
            val iterator = children.iterator()
            iterator.forEach {
                //DecorView一般情况下包含一个竖直方向的LinearLayout
                //(和状态栏及导航栏)
                Log.d("DecorView.children", it.toString())
            }
            val linearLayout = decorView.getChildAt(0) as ViewGroup
            Log.d("DecorView.childAt_0", linearLayout.toString())

            Log.d("childAt0.childCount", linearLayout.childCount.toString())
            val children1 = linearLayout.children
            val iterator1 = children1.iterator()
            iterator1.forEach {
                //LinearLayout则一般包含标题栏及内容栏(R.id.content)
                //内容栏也是一个FrameLayout
                Log.d("childAt0.children", it.toString())
            }

            val contentView = linearLayout.getChildAt(1) as ViewGroup
            Log.d("childAt0.childAt_1", contentView.toString())
            val id = contentView.id
            Log.d("childAt0.childAt_1_id", id.toString())

            Log.d("childAt0.childCount", contentView.childCount.toString())
            val children2 = contentView.children
            val iterator2 = children2.iterator()
            iterator2.forEach {
                Log.d("childAt1.children", it.toString())
            }

            val content = findViewById<ViewGroup>(android.R.id.content)
            Log.d("Content", content.toString())
            val contentChildren = content.children
            val iterator3 = contentChildren.iterator()
            iterator3.forEach {
                Log.d("content.children", it.toString())
            }

        }
    }
}