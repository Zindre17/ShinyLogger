package com.aarstrand.zindre.pokechecklist.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.tools.Tools
import com.aarstrand.zindre.pokechecklist.tools.Tools.Companion.getSizedBitmap
import com.aarstrand.zindre.pokechecklist.tools.Tools.Companion.root2

class DexItem(context: Context, attrs: AttributeSet): View(context, attrs){

    private var mImage: Int
    private var mName: String?
    private var mCount: Int
    private var mType1: Int
    private var mType2: Int
    private var mNumber: Int
    private var mNumString: String

    private var mBallBitmap: Bitmap? = null
    private var mImageBitmap: Bitmap? = null
    private var mType1Bitmap: Bitmap? = null
    private var mType2Bitmap: Bitmap? = null

    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.DexItem,
                0, 0).apply {
            try {
                mImage = getResourceId(R.styleable.DexItem_image, 0)
                mName = getString(R.styleable.DexItem_name)
                mCount = getInteger(R.styleable.DexItem_count,0)
                mType1 = getInteger(R.styleable.DexItem_type1, 0)
                mType2 = getInteger(R.styleable.DexItem_type2, 0)
                mNumber = getInteger(R.styleable.DexItem_number, 0)
                mNumString = String.format("#%03d",mNumber)
            } finally {
                recycle()
            }
        }
    }
    
    private var box: Path = Path()
    private val nameBoxPaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ff3e00")
        style = Paint.Style.FILL
    }

    private var nameX: Float = 0f
    private var nameY: Float = 0f
    private var nameTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        typeface = Typeface.DEFAULT
    }
    private var numX: Float = 0f
    private var numY: Float = 0f
    private var numPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        typeface = Typeface.DEFAULT
        color = Color.parseColor("#353535")
    }

    private var countX: Float = 0f
    private var countY: Float = 0f
    private var countPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        color = Color.WHITE
    }

    private var imageX: Float = 0f
    private var imageY: Float = 0f
    private var imageSize: Float = 0f

    private var ballX: Float = 0f
    private var ballY: Float = 0f
    private var ballSize: Float = 0f

    private var type1X: Float = 0f
    private var type1Y: Float = 0f
    private var type1Size: Float = 0f

    private var type2X: Float = 0f
    private var type2Y: Float = 0f
    private var type2Size: Float = 0f

    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var circleRadius: Float = 0f
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

        imageX = paddingStart + circleRadius - circleRadius * .95f / root2
        imageY = paddingTop + circleRadius - circleRadius * .95f / root2
        imageSize = Tools.root2 * circleRadius*.95f
        mImageBitmap = getSizedBitmap(this.context, mImage, imageSize, imageSize)

        ballX = paddingStart.toFloat()
        ballY = paddingTop.toFloat()
        ballSize = unit*1.2f
        mBallBitmap = getSizedBitmap(this.context, R.drawable.pokeball, ballSize, ballSize)

        countPaint.textSize = unit/2f
        countY = paddingTop.toFloat() + countPaint.textSize/2f
        countX = paddingStart + unit*1.25f

        type1X = w - paddingEnd - 3*unit/2f
        type1Y = ballY
        type1Size = unit
        mType1Bitmap = getSizedBitmap(this.context, mType1, type1Size, type1Size)

        type2X = 0f
        type2Y = 0f
        type2Size = type1Size
        mType2Bitmap = getSizedBitmap(this.context, mType2, type2Size, type2Size)
        nameTextPaint.textSize = unit

        nameX = paddingStart + unit*6
        nameY = paddingTop + unit*1.5f

        numX = nameX
        numY = nameY + unit *1.5f
        numPaint.textSize = unit*.8f

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
            drawText(mNumString, numX, numY, numPaint)
            if(mCount>0)drawText(String.format("x%d", mCount), countX, countY, countPaint)
        }
    }
}