package com.aarstrand.zindre.pokechecklist.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.aarstrand.zindre.pokechecklist.R
import com.aarstrand.zindre.pokechecklist.tools.Tools
import com.aarstrand.zindre.pokechecklist.tools.Tools.getSizedBitmap
import com.aarstrand.zindre.pokechecklist.tools.Tools.root2
import kotlin.math.min


class DexItem(context: Context, attrs: AttributeSet): View(context, attrs){

    private val NO_RESOURCE: Int = -1

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

    fun getName() = mName
    fun setName(name: String?){
        mName = name
        invalidate()
    }

    fun getImage() = mImage
    fun setImage(imageId: Int){
        mImage = imageId
        invalidate()
    }
    fun setImage(array: ByteArray){
        mImage = NO_RESOURCE
        mImageBitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
        if(imageSize!=0f)
            mImageBitmap = getSizedBitmap(context, mImageBitmap!!, imageSize, imageSize)
    }
    fun setImage(name: String){
        mImage = resources.getIdentifier(name, "raw", context.packageName)
        invalidate()
    }

    fun getNumber() = mNumber
    fun setNumber(number: Int){
        mNumber= number
        mNumString = String.format("#%03d",mNumber)
        invalidate()
    }

    fun getCount() = mCount
    fun setCount(count: Int){
        mCount = count
        invalidate()
    }

    fun getType1() = mType1
    fun setType1(type: Int){
        mType1 = type
        invalidate()
    }

    fun getType2() = mType2
    fun setType2(type: Int){
        mType2 = type
        invalidate()
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        var w = MeasureSpec.getSize(widthMeasureSpec)

        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        var h = MeasureSpec.getSize(heightMeasureSpec)

        if(wMode == MeasureSpec.EXACTLY && hMode == MeasureSpec.EXACTLY){
            //both are specified, no need to measure
            val contentWidth = w - paddingStart - paddingEnd
            val contentHeight = h - paddingTop - paddingBottom
            val unit = min((contentWidth/2.4f).toInt(), contentHeight)
            val verticalPad = ((h - unit)/2f).toInt()
            val horPad = ((w - unit * 2.4f)/2f).toInt()
            setPadding(horPad, verticalPad, horPad, verticalPad)
        }else if(wMode == MeasureSpec.EXACTLY && hMode == MeasureSpec.UNSPECIFIED){
            //width set, height can be whatever
            val contentWidth = w - paddingStart - paddingEnd
            h = (contentWidth/2.4f).toInt() + paddingBottom + paddingTop
        }else if(hMode == MeasureSpec.EXACTLY && wMode == MeasureSpec.UNSPECIFIED){
            //height set, width can be whatever
            val contentHeight = h - paddingTop - paddingBottom
            w = (contentHeight * 2.4f).toInt() + paddingStart + paddingEnd
        }else if(wMode == MeasureSpec.EXACTLY && hMode == MeasureSpec.AT_MOST){
            //width set, height has a maximum
            val maxContentWidth = w - paddingStart - paddingEnd
            val maxContentHeight = h - paddingTop - paddingBottom
            val recHeight = (maxContentWidth/2.4f).toInt()
            if(maxContentHeight < recHeight){
                val diff = ((maxContentWidth - maxContentHeight * 2.4f)/2f).toInt()
                setPadding(paddingLeft+diff, paddingTop, paddingRight+diff, paddingBottom)
            }else{
                h = recHeight + paddingBottom + paddingTop
            }

        }else if(hMode == MeasureSpec.EXACTLY && wMode == MeasureSpec.AT_MOST){
            //height set, width has a maximum
            val maxContentWidth = w - paddingStart - paddingEnd
            val maxContentHeight = h - paddingTop - paddingBottom
            val recWidth= (maxContentHeight * 2.4f).toInt()
            if(maxContentWidth < recWidth){
                val diff = ((maxContentHeight - maxContentWidth/2.4f)/2f).toInt()
                setPadding(paddingLeft, paddingTop+diff, paddingRight, paddingBottom+diff)
            }else{
                w = recWidth+ paddingStart+ paddingEnd
            }
        }else {

        }
        Log.v("Chart onMeasure w",MeasureSpec.toString(widthMeasureSpec))
        Log.v("Chart onMeasure h",MeasureSpec.toString(heightMeasureSpec))

        Log.v("SuggestedMinimumHeight", suggestedMinimumHeight.toString())
        Log.v("SuggestedMinimumWidth", suggestedMinimumWidth.toString())
        setMeasuredDimension(w,h)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val xpad = (paddingStart + paddingEnd).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        //val size =  hh
        //val horPad = (w - size*2.4f) /2f
        //val verPad = (h - size) /2f
        //setPadding(horPad.toInt(), verPad.toInt(), horPad.toInt(), verPad.toInt())

        val unit = hh/5f

        circleRadius = hh/2f
        circleX = paddingStart + circleRadius
        circleY = paddingTop + circleRadius

        imageX = paddingStart.toFloat() //circleRadius - circleRadius * .95f / root2
        imageY = paddingTop.toFloat() //circleRadius - circleRadius * .95f / root2
        imageSize = circleRadius*2f//root2 * circleRadius*.95f
        mImageBitmap =
                if(mImage==NO_RESOURCE && mImageBitmap!=null)
                    getSizedBitmap(this.context, mImageBitmap!!, imageSize, imageSize)
                else
                    getSizedBitmap(this.context, mImage, imageSize, imageSize)

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

    val borderPaint = Paint(ANTI_ALIAS_FLAG).apply{
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            drawPath(box, nameBoxPaint)
            drawCircle(circleX, circleY, circleRadius*.95f,circlePaint)
            if(mImageBitmap!=null)drawBitmap(mImageBitmap!!, imageX, imageY, null)
            //drawRect(imageX, imageY, imageX+imageSize, imageY + imageSize, borderPaint)
            if(mCount!=0){
                if(mBallBitmap!=null)drawBitmap(mBallBitmap!!, ballX, ballY, null)
                drawText(String.format("x%d", mCount), countX, countY, countPaint)
            }
            if(mType1Bitmap!=null)drawBitmap(mType1Bitmap!!, type1X, type1Y, null)
            if(mType2Bitmap!=null)drawBitmap(mType2Bitmap!!, type2X, type2Y, null)
            drawText(mName?:"", nameX, nameY, nameTextPaint)
            drawText(mNumString, numX, numY, numPaint)
        }
    }
}