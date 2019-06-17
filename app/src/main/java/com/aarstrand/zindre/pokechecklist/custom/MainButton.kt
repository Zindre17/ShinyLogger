package com.aarstrand.zindre.pokechecklist.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.tools.Tools
import com.aarstrand.zindre.pokechecklist.tools.Tools.getSizedBitmap

class MainButton(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //xml values
    private var mFlipped: Boolean
    private var mText: String?
    private var mImage: Int
    private var mPosition: Int
    private var mPadding: Float

    private var mBitmap: Bitmap? = null

    init{
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.MainButton,
                0,0).apply{
            try{
                mFlipped = getBoolean(R.styleable.MainButton_flipped, false)
                mText = getString(R.styleable.MainButton_text)
                mImage = getResourceId(R.styleable.MainButton_image, 0)
                mPosition = getInteger(R.styleable.MainButton_align, 3) //horCenter & verCenter
                mPadding = getDimension(R.styleable.MainButton_paddingOnLimitation, 0f)
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

    public fun getImage():Int{
        return mImage
    }

    public fun setImage(imageId:Int){
        mImage = imageId
        mBitmap = getSizedBitmap(this.context, mImage, imageSize, imageSize)
        invalidate()
        requestLayout()
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

    private var imageX: Float = 0f
    private var imageY: Float = 0f
    private var imageSize: Float = 0f

    //path for the box with semi-circle at the end
    private var path: Path = Path()
    //path for the circle with spike
    private var circle: Path = Path()
    //path for the border of the circle
    private var circleBorder: Path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Account for padding
        val xpad = (paddingStart + paddingEnd).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        var ww = w.toFloat() - xpad
        var hh = h.toFloat() - ypad

        // Figure out how big we can make the pie. minimum of entire height or 1/4 of width
        val diameter: Float

        val addPadding = paddingStart == 0 && paddingEnd == 0 && paddingTop == 0 && paddingBottom == 0
        if(ww/4f < hh){
            //width is limitation
            if(addPadding) {
                ww -= mPadding*2
                setPadding(mPadding.toInt(), 0, mPadding.toInt(), 0)
            }
            diameter = ww / 4f
            circleRadius = diameter/2f
            cx = if(mFlipped){
                w - paddingEnd.toFloat() - circleRadius
            }else{
                paddingStart.toFloat() + circleRadius
            }


            cy = when {
                mPosition and 2 == 2 -> //vertical center
                    paddingTop.toFloat() + hh/2f
                mPosition and 16 == 16 -> //top
                    paddingTop.toFloat() + circleRadius
                mPosition and 32 == 32 -> //bottom
                    h - paddingBottom.toFloat() - circleRadius
                else -> paddingTop.toFloat() + hh/2f
            }
        }else{
            //height is limitation
            if(addPadding){
                hh -= mPadding*2
                setPadding(0,mPadding.toInt(), 0, mPadding.toInt())
            }
            diameter = hh
            circleRadius = diameter/2f
            cy = paddingTop.toFloat() + hh/2f
            cx = when {
                mPosition and 1 == 1 -> //horizontal center
                    if(mFlipped){
                        w/2f + 3*circleRadius
                    }else{
                        w/2f - 3*circleRadius
                    }
                mPosition and 4 == 4 -> //left
                    if(mFlipped){
                        paddingStart.toFloat() + 7*circleRadius
                    }else{
                        paddingStart.toFloat() + circleRadius
                    }
                mPosition and 8 == 8 -> //right
                    if(mFlipped){
                        w - paddingEnd.toFloat() - circleRadius
                    }else{
                        w - paddingEnd.toFloat() - 7*circleRadius
                    }
                else -> if(mFlipped) {
                    w / 2f + 3 * circleRadius
                }else {
                    w / 2f - 3 * circleRadius
                }
            }
        }
        bt = cy - circleRadius / 2f
        bb = bt + circleRadius

        imageSize = circleRadius * Tools.root2
        mBitmap = getSizedBitmap(this.context, mImage, imageSize, imageSize)

        textX = if(mFlipped){
            cx - 3.5f*circleRadius
        }else{
            cx + 3.5f*circleRadius
        }
        textY = cy
        textPaint.textSize = diameter / 4f

        strokePaint.strokeWidth = diameter/20f
        val bwidth = strokePaint.strokeWidth/2f

        textY += textPaint.textSize/3f

        if(mFlipped){

            br = cx
            bl = br - 3*diameter

            path.reset()
            path.moveTo(br,bb)
            path.lineTo(bl,bb)
            path.arcTo(bl-circleRadius/2f, bt, bl + circleRadius /2f, bb, 90f, 180f, false)
            path.lineTo(br,bt)
            path.close()

            circle.reset()
            circle.arcTo(cx - circleRadius, cy - circleRadius, cx + circleRadius, cy + circleRadius, 135f, -270f, false)
            circle.lineTo(cx - circleRadius * 1.5f, cy)
            circle.close()


            circleBorder.reset()
            circleBorder.arcTo(cx - circleRadius + bwidth, cy - circleRadius + bwidth, cx + circleRadius - bwidth, cy + circleRadius - bwidth, 135f, -270f, false)
            circleBorder.lineTo(cx - circleRadius * 1.5f + bwidth, cy)
            circleBorder.close()


        }else {

            bl = cx
            br = bl + 3 * diameter

            path.reset()
            path.moveTo(bl, bb)
            path.lineTo(br, bb)
            path.arcTo(br - circleRadius / 2f, bt, br + circleRadius / 2f, bb, 90f, -180f, false)
            path.lineTo(bl, bt)
            path.close()

            circle.reset()
            circle.arcTo(cx - circleRadius, cy - circleRadius, cx + circleRadius, cy + circleRadius, 45f, 270f, false)
            circle.lineTo(cx + circleRadius * 1.5f, cy)
            circle.close()

            circleBorder.reset()
            circleBorder.arcTo(cx - circleRadius + bwidth, cy - circleRadius + bwidth, cx + circleRadius - bwidth, cy + circleRadius - bwidth, 45f, 270f, false)
            circleBorder.lineTo(cx + circleRadius * 1.5f - bwidth, cy)
            circleBorder.close()

        }

        imageX = cx - imageSize/2f
        imageY = cy - imageSize/2f
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
            drawPath(circleBorder, strokePaint)
            drawText(mText?:"" ,textX, textY, textPaint)
            if(mBitmap!=null) drawBitmap(mBitmap,imageX, imageY,null)
        }

    }

}