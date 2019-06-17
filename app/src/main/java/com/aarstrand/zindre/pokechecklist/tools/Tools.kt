package com.aarstrand.zindre.pokechecklist.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.RectF

class Tools {
    companion object{
        val root2: Float = Math.sqrt(2.0).toFloat()

        fun getSizedBitmap(context: Context, imageId: Int, width: Float, height: Float):Bitmap?{
            val b = BitmapFactory.decodeResource(context.resources, imageId) ?: return null
            val matrix = Matrix()

            val src = RectF(0f,0f, b.width.toFloat(), b.height.toFloat())
            val dst = RectF(0f, 0f, width, height)

            matrix.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER)

            return Bitmap.createBitmap(b, 0,0, b.width, b.height, matrix, true)
        }
    }
}