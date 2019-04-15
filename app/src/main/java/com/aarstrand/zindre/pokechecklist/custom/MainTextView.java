package com.aarstrand.zindre.pokechecklist.custom;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.aarstrand.zindre.pokechecklist.R;

import androidx.annotation.Nullable;

public class MainTextView extends View {

    private Path mShadowArc, mShadowLeftDiag,mShadowRightDiag, mShadowLine;
    private Paint mShadowArcPaint, mShadowLeftDiagPaint, mShadowRightDiagPaint, mShadowLinePaint;
    private int mShadowWidth = 20;

    private Path mTextBoxPath;
    private Paint mTextBoxPaint, mTextBoxBorderPaint;
    private int mTextBoxColor, mTextBoxBorderColor;
    private float mBorderWidth;

    private float mImageX, mImageY, mImageR;
    private Paint mImageBackgroundPaint;
    private int mImageBackgroundColor;
    private Paint mImagePaint;
    private Bitmap mImage;
    private Matrix mMatrix;

    private float mTextX, mTextY;
    private String mText;
    private Paint mTextPaint;
    private int mTextColor;
    private float mTextSize;
    private Paint.Align mTextAlign;

    private boolean mFlipped;

    private Path mPath;

    public MainTextView(Context context) {
        super(context);
        init(context, null);
    }

    public MainTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs){

        //---- retrieve xml attributes or set default ones ----\\
        if(attrs == null){
            mTextBoxColor = Color.BLUE;
            mTextBoxBorderColor = Color.BLACK;
            mBorderWidth = 0f;

            mImageBackgroundColor = Color.WHITE;
            mImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.pokeball);

            mTextColor = Color.BLACK;
            mText = "default";
            mTextAlign = Paint.Align.LEFT;
            mTextSize = 12f;

            mFlipped = false;
        }
        else {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.MainTextView,
                    0, 0
            );
            try{
                mTextBoxColor = a.getColor(R.styleable.MainTextView_textBoxColor, Color.BLUE);
                mTextBoxBorderColor = a.getColor(R.styleable.MainTextView_textBoxBorderColor, Color.RED);
                mBorderWidth = a.getFloat(R.styleable.MainTextView_textBoxBorderWidth, 5f);

                mImageBackgroundColor = a.getColor(R.styleable.MainTextView_ImageBackgroundColor, Color.WHITE);
                mImage = BitmapFactory.decodeResource(context.getResources(), a.getResourceId(R.styleable.MainTextView_imageSrc, R.drawable.pokeball));

                mTextColor = a.getColor(R.styleable.MainTextView_textColor, Color.BLACK);
                mText = a.getString(R.styleable.MainTextView_text);
                if(mText ==null) mText = "default";
                mTextSize = a.getDimension(R.styleable.MainTextView_textSize, 30f);
                int align = a.getInt(R.styleable.MainTextView_textAlign, 0);
                if(align == 0)
                    mTextAlign = Paint.Align.LEFT;
                else if(align == 1)
                    mTextAlign = Paint.Align.CENTER;
                else
                    mTextAlign = Paint.Align.RIGHT;

                mFlipped = a.getBoolean(R.styleable.MainTextView_flipped, false);
            }
            finally{
                a.recycle();
            }
        }

        //---- set up text box paints ----\\
        mTextBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBoxPaint.setColor(mTextBoxColor);
        mTextBoxPaint.setStyle(Paint.Style.FILL);
        mTextBoxBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBoxBorderPaint.setColor(mTextBoxBorderColor);
        mTextBoxBorderPaint.setStyle(Paint.Style.STROKE);
        //mTextBoxBorderPaint.setStrokeJoin(Paint.Join.BEVEL);
        mTextBoxBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mTextBoxBorderPaint.setStrokeWidth(mBorderWidth);
        //mTextBoxBorderPaint.setShadowLayer(10,5,5, Color.BLACK);

        //---- set up text paint ----\\
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        //---- set up image paints ----\\
        mImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImageBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mImageBackgroundPaint.setColor(mImageBackgroundColor);
        mImageBackgroundPaint.setStrokeWidth(5f);
        mImageBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mShadowArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowArcPaint.setStyle(Paint.Style.FILL);
        mShadowLeftDiagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowLeftDiagPaint.setStyle(Paint.Style.FILL);
        mShadowRightDiagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowRightDiagPaint.setStyle(Paint.Style.FILL);
        mShadowLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidate();

        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop()+getPaddingBottom());

        //--- set up shadows ---\\
        float ir = Math.min((h-ypad)/2f-mShadowWidth, (w-xpad)/6f);
        float centerx = getPaddingLeft()+ir;
        float centery = h/2f;
        float end = w-getPaddingRight()-mShadowWidth;

        Path sp = new Path();
        sp.moveTo(centerx-ir,centery);
        RectF arcRect = new RectF(
                centerx-ir,
                centery-ir+mShadowWidth,
                centerx+ir+mShadowWidth,
                centery+ir+mShadowWidth
        );
        RectF arcRect2 = new RectF(
                centerx-ir,
                centery-ir,
                centerx+ir,
                centery+ir
        );
        sp.arcTo(arcRect, 180,-135);
        sp.arcTo(arcRect2, 45,135);
        sp.close();
        mShadowArc = sp;

        int bl = 0xff101010;
        int tr = 0x00000000;
        Shader s = new RadialGradient(
                centerx,
                centery,
                ir+mShadowWidth,
                new int[]{bl,tr},
                new float[]{0.6f,1f},
                Shader.TileMode.CLAMP
        );
        mShadowArcPaint.setShader(s);

        bl = 0xaa101010;
        Path sb = new Path();
        float kat = ir/(float)Math.sqrt(2);
        float sx = centerx+kat;
        float sy = centery+kat;
        sb.moveTo(sx,sy);
        sb.lineTo(sx,sy+mShadowWidth);
        sb.lineTo(centerx+ir,centery+ir+mShadowWidth);
        sb.lineTo(centerx+ir,centery+ir);
        sb.lineTo(sx,sy);
        sb.close();
        mShadowLeftDiag = sb;

        Shader bs = new LinearGradient(
                sx,
                sy,
                sx-(float)Math.cos(45)*mShadowWidth,
                sy+(float)Math.sin(45)*mShadowWidth,
                bl,tr,
                Shader.TileMode.CLAMP
        );
        mShadowLeftDiagPaint.setShader(bs);

        Path sl = new Path();
        sl.moveTo(centerx+ir,centery+ir);
        sl.lineTo(centerx+ir, centery+ir+mShadowWidth);
        sl.lineTo(end-ir*2,centery+ir+mShadowWidth);
        sl.lineTo(end-ir*2,centery+ir);
        sl.lineTo(centerx+ir,centery+ir);
        sl.close();
        mShadowLine = sl;

        Shader sls = new LinearGradient(
                centerx+ir,
                centery+ir,
                centerx+ir,
                centery+ir+mShadowWidth,
                bl,
                tr,
                Shader.TileMode.CLAMP
        );
        mShadowLinePaint.setShader(sls);

        Path srd = new Path();
        srd.moveTo(end-ir*2,centery+ir);
        srd.lineTo(end-ir*2,centery+ir+mShadowWidth);
        srd.lineTo(end+mBorderWidth/2f, centery+mShadowWidth);
        srd.lineTo(end+mBorderWidth/2f, centery);
        srd.lineTo(end-ir*2,centery+ir);
        srd.close();
        mShadowRightDiag = srd;

        Shader srds = new LinearGradient(
                end-ir*2,centery+ir,
                end-(ir*2)+mShadowWidth/2f,centery+ir+mShadowWidth,
                bl,tr,
                Shader.TileMode.CLAMP
        );
        mShadowRightDiagPaint.setShader(srds);


        mImageR = ir - mBorderWidth;
        mImageX = centerx;
        mImageY = centery;

        int height = mImage.getHeight();
        int width = mImage.getWidth();

        float scale;
        if(height > width){
            scale = ((float)h)/height*.9f;
        }else{
            scale = ((float)h)/width*.9f;
        }
        float xOffset, yOffset;
        xOffset = ((h/scale)-width)/2;
        yOffset = ((h/scale)-height)/2;

        mMatrix = new Matrix();
        mMatrix.postScale(scale, scale);
        mMatrix.preTranslate(xOffset, yOffset);

        Path path = new Path();
        float hb = mBorderWidth/2f;
        path.moveTo(sx+hb,sy);
        path.lineTo(centerx+ir,centery+ir-hb);
        path.lineTo(end-ir*2,centery+ir-hb);
        path.lineTo(end-hb, centery);
        path.lineTo(end-ir,centery-kat+hb);
        path.lineTo(sx+hb,centery-kat+hb);
        path.close();
        mTextBoxPath = path;

        Path p = new Path();
        float r = h/2f;
        //p.addCircle(r, r, r, Path.Direction.CW);
        p.moveTo(r+r*(float)Math.cos(45d), h*3f/4f);
        p.lineTo(h, h);
        p.lineTo(w*0.75f, h);
        p.lineTo(w,r);
        p.lineTo(w*0.875f,h/4f);
        p.lineTo(r+r*(float)Math.cos(45d),h/4f);
        p.close();
        mPath = p;

        mTextX = w/2f;
        mTextY = mImageR/2f+(h-mImageR/2f)/2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 300;
        int desiredHeight = 150;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //measure Width
        if(widthMode == MeasureSpec.EXACTLY){
            //Must be this width
            width = widthSize;
        } else if(widthMode == MeasureSpec.AT_MOST){
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        } else if(heightMode == MeasureSpec.AT_MOST){
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        //must be called!
        setMeasuredDimension(width, height);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //canvas.drawPath(mShadowArc, mShadowArcPaint);
        //canvas.drawPath(mShadowLeftDiag, mShadowLeftDiagPaint);
        //canvas.drawPath(mShadowLine, mShadowLinePaint);
        //canvas.drawPath(mShadowRightDiag, mShadowRightDiagPaint);
        //canvas.drawPath(mTextBoxPath, mTextBoxBorderPaint);
        Matrix m = new Matrix();
        int c = mTextBoxPaint.getColor();
        m.preTranslate(0,10);
        mTextBoxPath.transform(m);
        mTextBoxPaint.setColor(0xff101010);
        mTextBoxPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
        canvas.drawPath(mTextBoxPath, mTextBoxPaint);
        m.postTranslate(0,-10);
        mTextBoxPaint.setMaskFilter(null);
        mTextBoxPath.transform(m);
        mTextBoxPaint.setColor(c);
        canvas.drawPath(mTextBoxPath, mTextBoxPaint);
        //canvas.drawCircle(mImageX, mImageY, mImageR, mTextBoxBorderPaint);
        //canvas.drawCircle(mImageX, mImageY, mImageR, mTextBoxPaint);

        //canvas.drawBitmap(mImage, mMatrix, mImagePaint);
        //canvas.drawText(mText.toUpperCase(), mTextX, mTextY, mTextPaint);
    }
}
