package com.example.customdrugview

import android.util.Log
import android.view.View
import android.view.View.OnClickListener

/**
 * Author: ChenGuiPing
 * Date: 2023/4/18
 * Description:
 */
class ClickProxy(var listener: OnClickListener?) : OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View?) {
        Log.d("ClickProxy", "Hook View Point")
        val cur = System.currentTimeMillis()
        if (cur - lastTime > 1000) {
            Log.d("ClickProxy", "防抖时间正常，传递点击事件")
            listener?.onClick(v)
            lastTime = cur
        }
    }

}