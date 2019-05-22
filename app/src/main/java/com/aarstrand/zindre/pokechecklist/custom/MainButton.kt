package com.aarstrand.zindre.pokechecklist.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateInterpolator
import com.aarstrand.zindre.pokechecklist.R

class MainButton(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //xml values
    private var mFlipped: Boolean
    private var mText: String?
    private var mImage: Int


    private var image: Bitmap? = null


    init{
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MainButton,
                0,0).apply{
            try{
                mFlipped = getBoolean(R.styleable.MainButton_flipped, false)
                mText = getString(R.styleable.MainButton_text)
                mImage = getResourceId(R.styleable.MainButton_image, 0)
            } finally {
                recycle()
            }
        }
    }


    public fun isFlipped(): Boolean{
        return mFlipped
    }

    public fun setFlipped(flipped: Boolean){
        mFlipped = flipped
        invalidate()
        requestLayout()
    }

    public fun getText(): String{
        return mText?:""
    }

    public fun setText(text: String){
        mText = text
        invalidate()
        requestLayout()
    }

    public fun getImageId():Int{
        return mImage
    }

    public fun setImageId(imageId:Int){
        mImage = imageId
        setupImage()
        invalidate()
        requestLayout()
    }


    private fun setupImage(){

    }


    //for the positioning the circle
    private var circleRadius: Float = 0f
    private var cx: Float = 0f
    private var cy: Float = 0f

    //for the box
    private var bl: Float = 0f
    private var br: Float = 0f
    private var bb: Float = 0f
    private var bt: Float = 0f

    //for the text
    private var textX: Float = 0f
    private var textY: Float = 0f

    //path for the box with semi-circle at the end
    private var path: Path = Path()
    //path for the circle with spike
    private var circle: Path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Account for padding
        val xpad = (paddingStart + paddingEnd).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        // Figure out how big we can make the pie. minimum of entire height or 1/4 of width
        val diameter = Math.min(ww/4f, hh)

        circleRadius = diameter/2f
        cy = paddingTop.toFloat() + circleRadius
        bt = diameter / 4f + paddingTop.toFloat()
        bb = bt + circleRadius

        textX = w/2f
        textY = cy
        textPaint.textSize = diameter / 4f

        strokePaint.strokeWidth = diameter/20f

        textY += textPaint.textSize/3f

        if(mFlipped){
            cx = w - paddingEnd.toFloat() - circleRadius

            br = cx
            bl = br - 3*diameter

            path.reset()
            path.moveTo(br,bb)
            path.lineTo(bl,bb)
            path.arcTo(bl-circleRadius/2f, bt, bl + circleRadius /2f, bb, 90f, 180f, false)
            path.lineTo(br,bt)
            path.close()

            circle.reset()
            circle.arcTo(cx - circleRadius, paddingTop.toFloat(), cx + circleRadius, cy + circleRadius, 135f, -270f, false)
            circle.lineTo(cx - circleRadius * 1.5f, cy)
            circle.close()
        }else {
            cx = paddingStart.toFloat() + circleRadius

            bl = cx
            br = bl + 3 * diameter

            path.reset()
            path.moveTo(bl, bb)
            path.lineTo(br, bb)
            path.arcTo(br - circleRadius / 2f, bt, br + circleRadius / 2f, bb, 90f, -180f, false)
            path.lineTo(bl, bt)
            path.close()

            circle.reset()
            circle.arcTo(paddingStart.toFloat(), paddingTop.toFloat(), cx + circleRadius, cy + circleRadius, 45f, 270f, false)
            circle.lineTo(cx + circleRadius * 1.5f, cy)
            circle.close()
        }
    }


    //paints
    private val textPaint = TextPaint(ANTI_ALIAS_FLAG).apply{
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val boxPaint = Paint(ANTI_ALIAS_FLAG).apply{
        color = Color.parseColor("#ff3e00")
        style = Paint.Style.FILL
    }

    private val circlePaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val strokePaint = Paint(ANTI_ALIAS_FLAG).apply{
        color = Color.BLACK
        style = Paint.Style.STROKE
    }

    private val imagePaint = Paint(ANTI_ALIAS_FLAG)


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            //drawColor(Color.WHITE)
            drawPath(path,boxPaint)
            drawPath(path,strokePaint)
            //drawCircle(cx,cy,circleRadius, boxPaint)
            //drawCircle(cx,cy,circleRadius, strokePaint)
            drawPath(circle, circlePaint)
            drawPath(circle, strokePaint)
            drawText(mText,textX, textY, textPaint)
        }

    }

}