package com.pptv.thumbs_up_surfaceview.favor;

import java.util.Random;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;

import com.pptv.thumbs_up_surfaceview.R;


/**
 * @author LeiKang
 * @time 2017/1/19 点赞动画 红心的实体
 */
public class FavorModel
{
    /**
     * 红心的当前坐标
     */
    public PointF mPoint;

    /**
     * 移动动画
     */
    public ValueAnimator moveAnimation;

    /**
     * 缩放动画
     */
    public ValueAnimator scaleAnimaion;

    /**
     * 旋转动画
     */

    public ValueAnimator rorateAnimation;

    /**
     * 透明度
     */
    public int alpha = 255;//

    /**
     * 心图
     */

    private Bitmap bitmap;

    /**
     * 绘制Bitmap的矩阵 用来缩放，移动和旋转
     */
    private Matrix matrix;

    /**
     * 缩放系数
     */
    private float sf = 0;

    /**
     * 旋转角度
     */
    private float dregree;

    /**
     * 产生随机数
     */
    private Random random;

    /**
     * 爱心drawables集合
     */
    private int[] drawables;

    public boolean isEnd = false;// 是否结束

    public FavorModel(Context context, FavorView zanView)
    {
        drawables = new int[7];
        // 赋值给drawables
        drawables[0] = R.mipmap.love_red;
        drawables[1] = R.mipmap.love_little_blue;
        drawables[2] = R.mipmap.love_blue;
        drawables[3] = R.mipmap.love_green;
        drawables[4] = R.mipmap.love_orange;
        drawables[5] = R.mipmap.love_purple;
        drawables[6] = R.mipmap.love_pink;
        random = new Random();
        bitmap = BitmapFactory.decodeResource(context.getResources(), drawables[random.nextInt(7)]);
        matrix = new Matrix();
        init(new PointF(zanView.getWidth() * 3 / 4, zanView.getHeight() - bitmap.getHeight() / 2),
                new PointF((random.nextInt(zanView.getWidth())), 0));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void init(final PointF startPoint, PointF endPoint)
    {
        moveAnimation = ValueAnimator.ofObject(
                new BezierEvaluator(
                        new PointF(random.nextInt((int) startPoint.x * 2), Math.abs(endPoint.y - startPoint.y) / 2),
                        new PointF(random.nextInt((int) startPoint.x * 2), Math.abs(endPoint.y - startPoint.y) / 2)),
                startPoint, endPoint);
        moveAnimation.setDuration(3000);
        moveAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mPoint = (PointF) animation.getAnimatedValue();
                alpha = (int) ((float) mPoint.y / (float) startPoint.y * 255);
            }

        });
        moveAnimation.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                bitmap.recycle();
            }
        });
        scaleAnimaion = ValueAnimator.ofFloat(0, 1f).setDuration(700);
        scaleAnimaion.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                Float f = (Float) animation.getAnimatedValue();
                sf = f.floatValue();
            }
        });
        rorateAnimation = ValueAnimator.ofFloat(0f, 360f).setDuration(3000);
        rorateAnimation.setStartDelay(500);
        rorateAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                Float f = (Float) animation.getAnimatedValue();
                dregree = f.floatValue();
            }
        });
        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playTogether(scaleAnimaion, rorateAnimation, moveAnimation);
        finalSet.start();
    }

    /**
     *  用matrix 矩阵 来绘制view的冬瓜
     * @param canvas
     * @param p
     */
    public void draw(Canvas canvas, Paint p)
    {
        if (bitmap != null && alpha > 0)
        {
            p.setAlpha(alpha);
            matrix.setScale(sf, sf, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            matrix.postTranslate(mPoint.x - bitmap.getWidth() / 2, mPoint.y - bitmap.getHeight() / 2);
            matrix.postRotate(dregree, mPoint.x, mPoint.y);
            canvas.drawBitmap(bitmap, matrix, p);
        }
        else
        {
            isEnd = true;
        }
    }

    public void stop()
    {
        if (moveAnimation != null)
        {
            moveAnimation.cancel();
            moveAnimation = null;
        }
        if (scaleAnimaion != null)
        {
            scaleAnimaion.cancel();
            scaleAnimaion = null;
        }
        if (rorateAnimation != null)
        {
            rorateAnimation.cancel();
            rorateAnimation = null;
        }
    }
}
