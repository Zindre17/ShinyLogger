package com.aarstrand.zindre.pokechecklist.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.tools.Tools

class DexItem(context: Context, attrs: AttributeSet): View(context, attrs){

    private var mImage: Int
    private var mName: String?
    private var mCount: Int
    private var mType1: Int
    private var mType2: Int

    private var mBallBitmap: Bitmap? = null
    private var mImageBitmap: Bitmap? = null
    private var mType1Bitmap: Bitmap? = null
    private var mType2Bitmap: Bitmap? = null

    init {
        context.obtainStyledAttributes(
                attrs,
                R.styleable.DexItem,
                0, 0).apply {
            try {
                mImage = getResourceId(R.styleable.DexItem_image, 0)
                mName = getString(R.styleable.DexItem_name)
                mCount = getInteger(R.styleable.DexItem_count,0)
                mType1 = getInteger(R.styleable.DexItem_type1, 0)
                mType2 = getInteger(R.styleable.DexItem_type2, 0)

            } finally {
                recycle()
            }
        }
    }

    private fun getImage(id:Int, width: Float, height: Float):Bitmap?{
        val b = BitmapFactory.decodeResource(resources, id) ?: return null
        val matrix: Matrix = Matrix()

        val src = RectF(0f,0f, b.width.toFloat(), b.height.toFloat())
        val dst = RectF(0f, 0f, width, height)

        matrix.setRectToRect(src,dst, Matrix.ScaleToFit.CENTER)

        return Bitmap.createBitmap(b, 0,0, b.width, b.height, matrix, true)
    }

    var box: Path = Path()
    private val nameBoxPaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ff3e00")
        style = Paint.Style.FILL
    }

    var nameX: Float = 0f
    var nameY: Float = 0f
    private var nameTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT
    }

    var imageX: Float = 0f
    var imageY: Float = 0f
    var imageSize: Float = 0f

    var ballX: Float = 0f
    var ballY: Float = 0f
    var ballSize: Float = 0f

    var type1X: Float = 0f
    var type1Y: Float = 0f
    var type1Size: Float = 0f

    var type2X: Float = 0f
    var type2Y: Float = 0f
    var type2Size: Float = 0f

    var circleX: Float = 0f
    var circleY: Float = 0f
    var circleRadius: Float = 0f
    private val circlePaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val xpad = (paddingStart + paddingEnd).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        val size = Math.min(ww/2.4f, hh)
        val horPad = (w - size*2.4f) /2f
        val verPad = (h - size) /2f
        setPadding(horPad.toInt(), verPad.toInt(), horPad.toInt(), verPad.toInt())

        val unit = size/5f

        circleRadius = size/2f
        circleX = paddingStart + circleRadius
        circleY = paddingTop + circleRadius

        imageX = paddingStart + unit
        imageY = paddingTop + unit
        imageSize = Tools.root2 * circleRadius
        mImageBitmap = getImage(mImage, imageSize, imageSize)

        ballX = paddingStart.toFloat()
        ballY = paddingTop.toFloat()
        ballSize = unit
        mBallBitmap = getImage(R.drawable.pokeball, ballSize, ballSize)

        type1X = w - paddingEnd - 3*unit/2f
        type1Y = ballY
        type1Size = unit
        mType1Bitmap = getImage(mType1, type1Size, type1Size)

        type2X = 0f
        type2Y = 0f
        type2Size = type1Size
        mType2Bitmap = getImage(mType2, type2Size, type2Size)
        nameTextPaint.textSize = unit/3f * 2f
        if(mName!=null)
        while(nameTextPaint.measureText(mName) > unit*3)
            nameTextPaint.textSize *= .9f
        nameX = w - paddingEnd - size/2f
        nameY = h - paddingBottom - unit/5f

        box.reset()
        box.moveTo(paddingStart.toFloat(), h - paddingBottom.toFloat())
        box.lineTo(w - paddingEnd.toFloat(), h - paddingBottom.toFloat())
        box.lineTo(w - paddingEnd.toFloat(), paddingTop.toFloat())
        box.lineTo(paddingStart + 4*unit, paddingTop.toFloat())
        box.close()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawPath(box, nameBoxPaint)
            drawCircle(circleX, circleY, circleRadius*.95f,circlePaint)
            if(mImageBitmap!=null)drawBitmap(mImageBitmap, imageX, imageY, null)
            if(mBallBitmap!=null)drawBitmap(mBallBitmap, ballX, ballY, null)
            if(mType1Bitmap!=null)drawBitmap(mType1Bitmap, type1X, type1Y, null)
            if(mType2Bitmap!=null)drawBitmap(mType2Bitmap, type2X, type2Y, null)
            drawText(mName?:"", nameX, nameY, nameTextPaint)
        }
    }
}