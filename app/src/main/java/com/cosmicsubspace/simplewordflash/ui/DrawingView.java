package com.cosmicsubspace.simplewordflash.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

//http://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android

public class DrawingView extends View {


    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint   mBitmapPaint;
    Context context;

    private Paint linePaint, dotPaint;

    public DrawingView(Context c) {
        super(c);
        context=c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(12);

        dotPaint = new Paint();
        dotPaint.setAntiAlias(true);
        dotPaint.setDither(true);
        dotPaint.setColor(Color.BLACK);
        dotPaint.setStyle(Paint.Style.FILL);
    }

    public void clear(){
        mBitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255,255,255,255);
        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath( mPath, linePaint);
    }


    //last cursor position
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    //We need to check is a line has been drawn
    //or we can't draw dots.
    private boolean lineDrawn=false;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        lineDrawn=false;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
            lineDrawn=true;
        }
    }

    private void touch_up(float x, float y) {
        if (!lineDrawn){
            mCanvas.drawCircle(mX,mY, linePaint.getStrokeWidth()/1.414f,dotPaint);
        }


        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, linePaint);

        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up(x,y);
                invalidate();
                break;
        }
        return true;
    }
}
