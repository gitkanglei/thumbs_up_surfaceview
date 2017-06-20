package com.pptv.thumbs_up_surfaceview.favor;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 *
 * @author LeiKang
 * @time 2017/1/19
 */
public class FavorView extends SurfaceView implements SurfaceHolder.Callback
{

    private final SurfaceHolder surfaceHolder;

    /**
     * 心的个数
     */
    private ArrayList<FavorModel> zanBeen = new ArrayList<FavorModel>();

    private Paint p;

    /**
     * 负责绘制的工作线程
     */
    private DrawThread drawThread;

    public FavorView(Context context)
    {
        this(context, null);
    }

    public FavorView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public FavorView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.setZOrderOnTop(true);
        /** 设置画布 背景透明 */
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        p = new Paint();
        p.setAntiAlias(true);
        drawThread = new DrawThread();
    }

    /**
     * 点赞动作 添加心的函数 控制画面最大心的个数
     */
    public void addZanXin(FavorModel zanBean)
    {
        zanBeen.add(zanBean);
        if (zanBeen.size() > 30)
        {
            zanBeen.remove(0);
        }
        start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (drawThread == null)
        {
            drawThread = new DrawThread();
        }
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (drawThread != null)
        {
            drawThread = null;
        }
    }

    // 在单独一个线程中去绘制View
    class DrawThread extends Thread
    {
        boolean isRun = true;

        @Override
        public void run()
        {
            super.run();
            while (isRun)
            {
                Canvas canvas = null;
                synchronized (surfaceHolder)
                {
                    try
                    {
                        canvas = surfaceHolder.lockCanvas();
                        boolean isEnd = true;
                        /** 清除画面 */
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        /** 对所有心进行遍历绘制 */
                        for (int i = 0; i < zanBeen.size(); i++)
                        {
                            zanBeen.get(i).draw(canvas, p);
                            isEnd = zanBeen.get(i).isEnd;
                        }
                        if (isEnd)
                        {
                            isRun = false;
                            drawThread = null;
                        }
                        SystemClock.sleep(10);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (canvas != null)
                        {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }

                // // isRun = false;
                // synchronized (locked)
                // {
                // try
                // {
                // locked.wait();
                // }
                // catch (Exception e)
                // {
                // e.printStackTrace();
                // }
                // // drawThread.wait()
                // }
                // if (isDetached)
                // {
                // // break;
                // }
            }

        }
    }

    boolean isDetached = false;

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        isDetached = true;
    }

    public void stop()
    {
        if (drawThread != null)
        {

            for (int i = 0; i < zanBeen.size(); i++)
            {
                zanBeen.get(i).stop();
            }

            drawThread = null;
        }

    }

    public void start()
    {
        // drawThread = new DrawThread();
        if(drawThread ==null)
        {
            drawThread =new DrawThread();
            drawThread.start();
        }

    }
}
