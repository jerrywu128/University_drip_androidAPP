package com.example.drip_platform.ExtendComponent;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Electrocardiogram extends View {
    private float mWidth = 0,mHeight = 0;//自身大小
    private int mBackGroundColor_hart = Color.BLACK;
    private Paint mLinePaint;//画笔
    private Paint mWavePaint;//心电图的折现
    private Path mPath;//心电图的路径

    private ArrayList refreshList = new ArrayList();//后加的数据点
    private int row;//背景网格的行数和列数

    //心电
    private float MAX_VALUE = 100;
    private float WAVE_LINE_STROKE_WIDTH = 2;
    private int mWaveLineColor_hart = Color.parseColor("#EE4000");//波形颜色
    private  float nowX,nowY;//目前的xy坐标

    //网格
    private final int GRID_SMALL_WIDTH = 10;//每一个网格的宽度和高度,包括线
    private final int GRID_BIG_WIDTH = 50;//每一个大网格的宽度和高度,包括线
    private int xSmallNum,ySmallNum,xBigNum,yBigNum;//小网格的横格，竖格，大网格的横格，竖格数量
    private final int GRID_LINE_WIDTH=2;//网格的线的宽度
    private int mWaveSmallLineColor_hart = Color.parseColor("#092100");//小网格颜色
    private int mWaveBigLineColor_hart = Color.parseColor("#1b4200");//小网格颜色

    private int mBackGroundColor = Color.WHITE;
    private int mWaveSmallLineColor = Color.parseColor("#FFFFFF");
    private int mWaveBigLineColor = Color.parseColor("#000000");
    private int mWaveLineColor = Color.parseColor("#0062ff");//波形颜色


    private int Weight_or_heartbeat = 0;

    public Electrocardiogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Electrocardiogram(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Electrocardiogram(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
        //System.out.println(h);
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(GRID_LINE_WIDTH);
        mLinePaint.setAntiAlias(true);//抗锯齿效果

        mWavePaint = new Paint();
        mWavePaint.setStyle(Paint.Style.STROKE);
        mWavePaint.setColor(mWaveLineColor);
        mWavePaint.setStrokeWidth(WAVE_LINE_STROKE_WIDTH);
        mWavePaint.setAntiAlias(true);//抗锯齿效果

        mPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();//获取view的宽
        mHeight = getMeasuredHeight();//获取view的高

        row= (int) (mWidth/(GRID_SMALL_WIDTH));//获取行数

        //小网格
        xSmallNum = (int) (mHeight/GRID_SMALL_WIDTH);//横线个数=总高度/小网格高度
        ySmallNum = (int) (mWidth/GRID_SMALL_WIDTH);//竖线个数=总宽度/小网格宽度
        //大网格
        xBigNum = (int) (mHeight/GRID_BIG_WIDTH);//横线个数
        yBigNum = (int) (mWidth/GRID_BIG_WIDTH);//竖线个数
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(Weight_or_heartbeat == 0){
            //绘制网格
            drawGrid(canvas);
            //绘制波形
            drawWaveLine(canvas);
            mWavePaint.setColor(mWaveLineColor);
            mWavePaint.setStrokeWidth(10);
        }else if(Weight_or_heartbeat == 1){
            drawGrid_hart(canvas);
            drawWaveLine_heart(canvas);
            mWavePaint.setColor(mWaveLineColor_hart);
            mWavePaint.setStrokeWidth(WAVE_LINE_STROKE_WIDTH);
        }
    }

    /**
     * 画折线
     * @param canvas
     */
    private void drawWaveLine(Canvas canvas) {
        if(null == refreshList || refreshList.size()<=0){
            return;
        }
        mPath.reset();
        //mPath.moveTo(0f,mHeight/2);
        mPath.moveTo(0f,mHeight*0.1f);
        for (int i = 0;i<refreshList.size();i++){
            nowX = i* GRID_SMALL_WIDTH;
            float dataValue = (float) refreshList.get(i);
            if(dataValue <= mHeight*0.25f){
                    dataValue = mHeight * 0.25f;
            }else if(dataValue > mHeight){
                dataValue = mHeight * 0.8f;
            }
            //nowY = mHeight/2 + dataValue *(mHeight/(MAX_VALUE*2));
            nowY = mHeight - dataValue;
            mPath.lineTo(nowX,nowY);
        }
        canvas.drawPath(mPath, mWavePaint);
        if(refreshList.size()>row){
            refreshList.remove(0);
        }
    }

    private void drawWaveLine_heart(Canvas canvas) {
        if(null == refreshList || refreshList.size()<=0){
            return;
        }
        mPath.reset();
        mPath.moveTo(0f,mHeight*0.1f);
        for (int i = 0;i<refreshList.size();i++){
            nowX = i* GRID_SMALL_WIDTH;
            float dataValue = (float) refreshList.get(i);
            if(dataValue>0){
                if(dataValue > MAX_VALUE){
                    dataValue = MAX_VALUE * 0.9f;
                }
            }
            //nowY = mHeight/2 + dataValue *(mHeight/(MAX_VALUE*2));
            nowY = mHeight * (dataValue * 0.01f);
            mPath.lineTo(nowX,nowY);
            //System.out.println("datavalue:"+dataValue);
            //System.out.println("nowY:"+nowY);
        }
        canvas.drawPath(mPath, mWavePaint);
        if(refreshList.size()>row){
            refreshList.remove(0);
        }
    }

    //画网格
    private void drawGrid(Canvas canvas){

        canvas.drawColor(mBackGroundColor);
        //画小网格
        mLinePaint.setColor(mWaveSmallLineColor);
        //画横线
        for(int i = 0;i < xSmallNum + 1;i++){
            canvas.drawLine(0,i*GRID_SMALL_WIDTH,
                    mWidth, i*GRID_SMALL_WIDTH, mLinePaint);
        }
        //画竖线
        for(int i = 0;i < ySmallNum+1;i++){
            canvas.drawLine(i*GRID_SMALL_WIDTH,0,
                    i*GRID_SMALL_WIDTH,mHeight, mLinePaint);
        }
        //画大网格
        mLinePaint.setColor(mWaveBigLineColor);
        //画横线
        for(int i = 0;i < xBigNum + 1;i++){
            canvas.drawLine(0,i*GRID_BIG_WIDTH,
                    mWidth, i*GRID_BIG_WIDTH, mLinePaint);
        }
        //画竖线
        for(int i = 0;i < yBigNum+1;i++){
            canvas.drawLine(i*GRID_BIG_WIDTH,0,
                    i*GRID_BIG_WIDTH,mHeight, mLinePaint);
        }
    }

    private void drawGrid_hart(Canvas canvas){

        canvas.drawColor(mBackGroundColor_hart);
        //画小网格
        mLinePaint.setColor(mWaveSmallLineColor_hart);
        //画横线
        for(int i = 0;i < xSmallNum + 1;i++){
            canvas.drawLine(0,i*GRID_SMALL_WIDTH,
                    mWidth, i*GRID_SMALL_WIDTH, mLinePaint);
        }
        //画竖线
        for(int i = 0;i < ySmallNum+1;i++){
            canvas.drawLine(i*GRID_SMALL_WIDTH,0,
                    i*GRID_SMALL_WIDTH,mHeight, mLinePaint);
        }
        //画大网格
        mLinePaint.setColor(mWaveBigLineColor_hart);
        //画横线
        for(int i = 0;i < xBigNum + 1;i++){
            canvas.drawLine(0,i*GRID_BIG_WIDTH,
                    mWidth, i*GRID_BIG_WIDTH, mLinePaint);
        }
        //画竖线
        for(int i = 0;i < yBigNum+1;i++){
            canvas.drawLine(i*GRID_BIG_WIDTH,0,
                    i*GRID_BIG_WIDTH,mHeight, mLinePaint);
        }
    }

    public void showLine(float line, int Weight_or_heart) {
        Weight_or_heartbeat = Weight_or_heart;
        refreshList.add(line);
        postInvalidate();
    }
    //重置折现的坐标集合
    public void resetCanavas() {
        refreshList.clear();
    }




}
