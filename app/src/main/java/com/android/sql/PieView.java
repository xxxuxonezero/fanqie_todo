package com.android.sql;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PieView extends View {
    private List<PieItemBean> mDatas = new ArrayList<>();
    private float totalAngle = 360f;
    private float mLineLength = 40f;
    private float mWidthSide = 120f;//边距
    private float mTextLine = 80f;//横线的长度
    private float[] Degrees;//记录角度数组
    private Paint mPaint;
    private RectF rectF;
    private Paint mPaintBorder;
    private int mWidth;
    private int mHeight;
    private int cenX;
    private int cenY;
    private int mRadius;
    private int mSelectedPos = -1;//点击位置
    private OnClickListener listener;

    public PieView(Context context) {
        super(context);
        initView();
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);

        mPaintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setColor(Color.BLACK);
        mPaintBorder.setTextSize(35);
    }

    public void setData(List<PieItemBean> data){
        mDatas = data;
        Degrees= new float[mDatas.size()];//记录角度数组
        invalidate();
    }

    public interface OnClickListener{
        void onClick(PieItemBean pieItemBean);
    }
    public void setPieViewOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = resolveSizeValue(getMeasuredWidth(), widthMeasureSpec);
        mHeight = resolveSizeValue(getMeasuredHeight(), heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDatas == null || mDatas.size() ==0){
            return;
        }
        int width = mWidth - getPaddingLeft() - getPaddingRight() ;
        int height = mHeight - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width, height) / 2;
        //圆心坐标
        cenX = radius + getPaddingLeft();
        cenY = radius + getPaddingTop();
        if (width>height){
            cenX = width/2;
        }else if (width<height){
            cenY = height/2;
        }

        if (rectF == null) {
            rectF = new RectF(cenX - radius +mWidthSide, cenY - radius +mWidthSide,  radius + cenX -mWidthSide, radius + cenY -mWidthSide);
        }
        mRadius =  radius -(int) mWidthSide;

        float currentAngle = 0.0f;//圆心角度
        float offsetAngle = 0.0f;//角度偏移

        for (int i = 0; i < mDatas.size(); i++) {
            PieItemBean pieItemBean = mDatas.get(i);
            currentAngle = per2Radius(totalAngle, pieItemBean.value);
            mPaint.setColor(pieItemBean.color);

            if (mSelectedPos == i){
                canvas.save();
                canvas.translate(cenX,cenY);
                canvas.rotate(offsetAngle + currentAngle / 2);
                canvas.translate(30,0);
                RectF rectF = new RectF(-mRadius,-mRadius,mRadius,mRadius);
                //扇形
                canvas.drawArc(rectF,currentAngle/2,-currentAngle,true,mPaint);
                //边框
//				canvas.drawArc(rectF, currentAngle /2, -currentAngle, true, mPaintBorder);
                canvas.restore();
            }else {
                //扇形
                canvas.drawArc(rectF,offsetAngle,currentAngle,true,mPaint);
                //边框
//				canvas.drawArc(rectF, offsetAngle , currentAngle, true, mPaintBorder);
            }

            //线
            float arcCenterC = offsetAngle + currentAngle/2;//夹角
            float arcCenterX = 0;//起始点坐标x
            float arcCenterY = 0;//起始点坐标y

            float arcCenterX2 = 0;//结束点坐标
            float arcCenterY2 = 0;//结束点坐标
            //文字
            String s=mDatas.get(i).name+" "+mDatas.get(i).time+"分钟";
            float textSize = mPaintBorder.measureText(s);
            Rect rect = new Rect();
            mPaintBorder.getTextBounds(s,0,s.length(),rect);
            int textHeight = rect.height()/2;
//			Log.d("角度:",arcCenterC+"");
            if (arcCenterC >=0 && arcCenterC<90){
                arcCenterX =(float) (cenX + mRadius *Math.cos(arcCenterC*Math.PI/180));
                arcCenterY = (float)(cenY + mRadius *Math.sin(arcCenterC*Math.PI/180));
                arcCenterX2 = (float)(arcCenterX + mLineLength*Math.cos(arcCenterC*Math.PI/180));
                arcCenterY2 = (float)(arcCenterY + mLineLength*Math.sin(arcCenterC*Math.PI/180));
                canvas.drawLine(arcCenterX2,arcCenterY2,/*mWidth-mWidthSide*/cenX+mRadius+mTextLine,arcCenterY2,mPaintBorder);
                canvas.drawText(s,cenX+mRadius+mTextLine,arcCenterY2+textHeight,mPaintBorder);
            }else if (arcCenterC >=90 && arcCenterC<180){
                arcCenterC = 180 - arcCenterC;
                arcCenterX =(float) (cenX - mRadius *Math.cos(arcCenterC*Math.PI/180));
                arcCenterY = (float)(cenY + mRadius *Math.sin(arcCenterC*Math.PI/180));
                arcCenterX2 = (float)(arcCenterX - mLineLength*Math.cos(arcCenterC*Math.PI/180));
                arcCenterY2 = (float) (arcCenterY + mLineLength*Math.sin(arcCenterC*Math.PI/180));
                canvas.drawLine(arcCenterX2,arcCenterY2,cenX-mRadius-mTextLine,arcCenterY2,mPaintBorder);
                canvas.drawText(s,cenX-mRadius-mTextLine-textSize,arcCenterY2+textHeight,mPaintBorder);
            }else if (arcCenterC >= 180 && arcCenterC <270){
                arcCenterC = 270 - arcCenterC;
                arcCenterX =(float)(cenX - mRadius *Math.sin(arcCenterC*Math.PI/180));
                arcCenterY = (float) (cenY - mRadius *Math.cos(arcCenterC*Math.PI/180));
                arcCenterX2 = (float) (arcCenterX - mLineLength*Math.sin(arcCenterC*Math.PI/180));
                arcCenterY2 = (float) (arcCenterY - mLineLength*Math.cos(arcCenterC*Math.PI/180));
                canvas.drawLine(arcCenterX2,arcCenterY2,cenX-mRadius-mTextLine,arcCenterY2,mPaintBorder);
                canvas.drawText(s,cenX-mRadius-mTextLine-textSize,arcCenterY2+textHeight,mPaintBorder);
            }else if (arcCenterC >=270 && arcCenterC <360){
                arcCenterC = 360 - arcCenterC;
                arcCenterX = (float) (cenX + mRadius *Math.cos(arcCenterC*Math.PI/180));
                arcCenterY = (float) (cenY - mRadius *Math.sin(arcCenterC*Math.PI/180));
                arcCenterX2 = (float) (arcCenterX + mLineLength*Math.cos(arcCenterC*Math.PI/180));
                arcCenterY2 = (float) (arcCenterY - mLineLength*Math.sin(arcCenterC*Math.PI/180));
                canvas.drawLine(arcCenterX2,arcCenterY2,cenX+mRadius+mTextLine,arcCenterY2,mPaintBorder);
                canvas.drawText(s,cenX+mRadius+mTextLine,arcCenterY2+textHeight,mPaintBorder);
            }
            canvas.drawLine(arcCenterX,arcCenterY,arcCenterX2,arcCenterY2,mPaintBorder);

            offsetAngle += currentAngle;
            Degrees[i] = offsetAngle;
        }
    }

    private int resolveSizeValue(int size, int measureSpec){
        int result = 100;//最小值
        int spcMode = MeasureSpec.getMode(measureSpec);
        int spcSize = MeasureSpec.getSize(measureSpec);
        switch (spcMode){
            case MeasureSpec.UNSPECIFIED:
                return size;
            case MeasureSpec.AT_MOST:
                if (size<= spcSize){
                    return size;
                }else {
                    return spcSize;
                }
            case MeasureSpec.EXACTLY:
                return spcSize;
        }
        return result;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//				mStartTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float dx = x - cenX;
                float dy = y - cenY;
                float a = Math.abs(dx);
                float b = Math.abs(dy);
                double c = Math.toDegrees(Math.atan(b / a));
                float degree;
                if (isInCircle(dx,dy)){
                    if (dx>=0 && dy>=0){//one
                        degree = (float) c;
                    }else if (dx<=0 && dy>=0){//two
                        degree = 180 - (float) c;
                    }else if (dx<=0 && dy<= 0){//three
                        degree = 180+(float) c;
                    }else {//four
                        degree = 360 - (float) c;
                    }
                    mSelectedPos = whereDegree(degree);
                    listener.onClick(mDatas.get(mSelectedPos));
                    invalidate();

                }
                return false;
        }
        return true;
    }

    private int  whereDegree(float degree) {
        int selectPos = -1;
        for (int i = 0; i < Degrees.length; i++) {
            if (degree <=Degrees[i]){
                selectPos = i;
                break;
            }
        }
        return selectPos;
    }

    private boolean isInCircle(float dx, float dy) {
        double hypotenuse =Math.sqrt( Math.pow(Math.abs(dx), 2) + Math.pow(Math.abs(dy), 2));
        return hypotenuse < mRadius;
    }

    /**
     * 将百分比转换为图心角角度
     */
    public float per2Radius(float totalAngle, float percentage) {
        float angle = 0.0f;
        if (percentage >= 101f || percentage < 0.0f) {
            //Log.e(TAG,"输入的百分比不合规范.须在0~100之间.");
        } else {
            float v = percentage / 100;//先获取百分比
            float itemPer = totalAngle * v;//获取对应角度的百分比
            angle = round(itemPer, 2);//精确到小数点后面2位
        }
        return angle;
    }

    /**
     * 四舍五入到小数点后scale位
     */
    public float round(float v, int scale) {
        if (scale < 0)
            throw new IllegalArgumentException("The scale must be a positive integer or zero");

        BigDecimal bgNum1 = new BigDecimal(v);
        BigDecimal bgNum2 = new BigDecimal("1");
        return bgNum1.divide(bgNum2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
