package com.aarstrand.zindre.pokechecklist.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF


object Tools{
    val root2: Float = Math.sqrt(2.0).toFloat()

    fun getSizedBitmap(context: Context, imageId: Int, width: Float, height: Float):Bitmap?{
        if(width == 0f || height == 0f) return null
        val b = BitmapFactory.decodeResource(context.resources, imageId) ?: return null
        return getSizedBitmap(context, b, width, height)
    }

    fun getSizedBitmap(context: Context, image: Bitmap, width: Float, height: Float):Bitmap?{
        if(width == 0f || height == 0f) return null
        val matrix = Matrix()

        val src = RectF(0f,0f, image.width.toFloat(), image.height.toFloat())
        val dst = RectF(0f, 0f, width, height)

        matrix.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER)

        return Bitmap.createBitmap(image, 0,0, image.width, image.height, matrix, true)
    }

    val DEX_COUNT: Int = 721
}
