package com.aarstrand.zindre.pokechecklist.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.tools.Tools.getSizedBitmap

class CatchItem(context: Context, attrs: AttributeSet): View(context, attrs){

    private var mImage: Int
    private var mNick: String?
    private var mBall: Int
    private var mGame: Int

    private var mBallBitmap: Bitmap? = null
    private var mImageBitmap: Bitmap? = null
    private var mGameBitmap: Bitmap? = null

    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.CatchItem,
                0, 0).apply {
            try {
                mImage = getResourceId(R.styleable.CatchItem_image, 0)
                mNick = getString(R.styleable.CatchItem_nickname)
                mBall = getInteger(R.styleable.CatchItem_ball,0)
                mGame = getInteger(R.styleable.CatchItem_game, 0)

            } finally {
                recycle()
            }
        }
    }

    var nameBox: Path = Path()
    var nameX: Float = 0f
    var nameY: Float = 0f
    var nameTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT
    }
    private val nameBoxPaint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#ff3e00")
        style = Paint.Style.FILL
    }

    var imageX: Float = 0f
    var imageY: Float = 0f
    var imageSize: Float = 0f

    var ballX: Float = 0f
    var ballY: Float = 0f
    var ballSize: Float = 0f

    var gameX: Float = 0f
    var gameY: Float = 0f
    var gameSize: Float = 0f

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

        val size = Math.min(ww, hh)
        val horPad = (w - size) /2f
        val verPad = (h - size) /2f
        setPadding(horPad.toInt(), verPad.toInt(), horPad.toInt(), verPad.toInt())

        val unit = size/5f

        circleRadius = size/2f
        circleX = paddingStart + circleRadius
        circleY = paddingTop + circleRadius

        imageX = paddingStart + unit
        imageY = paddingTop + unit
        imageSize = 3*unit
        mImageBitmap = getSizedBitmap(this.context, mImage, imageSize, imageSize)

        ballX = paddingStart.toFloat()
        ballY = paddingTop.toFloat()
        ballSize = unit*1.2f
        mBallBitmap = getSizedBitmap(this.context, R.drawable.pokeball, ballSize, ballSize)

        gameX = w - paddingEnd - unit*1.2f
        gameY = ballY
        gameSize = unit*1.2f
        mGameBitmap = getSizedBitmap(this.context, R.drawable.pokeball_gray, gameSize, gameSize)

        nameTextPaint.textSize = unit/3f * 2f
        if(mNick!=null)
        while(nameTextPaint.measureText(mNick) > unit*3)
            nameTextPaint.textSize *= .9f
        nameX = w - paddingEnd - size/2f
        nameY = h - paddingBottom - unit/5f
        nameBox.reset()
        nameBox.moveTo(paddingStart.toFloat(), h - paddingBottom.toFloat())
        nameBox.lineTo(w - paddingEnd - unit, h - paddingBottom.toFloat())
        nameBox.arcTo(w - paddingEnd - unit,h - paddingBottom - unit, w - paddingEnd.toFloat(), h - paddingBottom.toFloat(), 90f,-180f, false )
        nameBox.lineTo(paddingStart + unit, h - paddingBottom - unit)
        nameBox.close()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawCircle(circleX, circleY, circleRadius,circlePaint)
            if(mImageBitmap!=null)drawBitmap(mImageBitmap, imageX, imageY, null)
            if(mBallBitmap!=null)drawBitmap(mBallBitmap, ballX, ballY, null)
            if(mGameBitmap!=null)drawBitmap(mGameBitmap, gameX, gameY, null)
            drawPath(nameBox, nameBoxPaint)
            drawText(mNick?:"", nameX, nameY, nameTextPaint)
        }
    }
}