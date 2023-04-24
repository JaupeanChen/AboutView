package com.example.customdrugview.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.annotation.DrawableRes

fun View.getBitMap(@DrawableRes res: Int, width: Int = 640): Bitmap = let {
    val option = BitmapFactory.Options()
    option.inJustDecodeBounds = true  //为什么先设为true，获取参数不加载进内存
    BitmapFactory.decodeResource(resources, res)

    option.inJustDecodeBounds = false
    option.inDensity = option.outWidth //取到图片实际的宽度
    option.inTargetDensity = width  //设置实际“像素密度”, 进行缩放
    BitmapFactory.decodeResource(resources, res, option)
}

fun View.getBackgroundBitmap(): Bitmap = let {
    this.buildDrawingCache()
    this.drawingCache
}