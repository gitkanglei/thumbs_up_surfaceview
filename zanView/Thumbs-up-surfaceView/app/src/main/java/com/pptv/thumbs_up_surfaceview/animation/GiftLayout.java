package com.pptv.thumbs_up_surfaceview.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pptv.thumbs_up_surfaceview.R;


/**
 * @author LeiKang
 * @time 2017/1/23
 */
public class GiftLayout extends FrameLayout
{
    private Context mContext;

    RelativeLayout anim_rl;

    ImageView anim_gift, anim_header;

    TextView anim_nickname, anim_sign;

    TextView anim_num;

    /**
     * 礼物数量的起始值
     */
    int starNum = 1;

    int repeatCount = 0;

    private boolean isShowing = false;

    public GiftLayout(Context context)
    {
        this(context, null);
    }

    public GiftLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView()
    {
        inflate(mContext, R.layout.gift_frame_layout, this);
        anim_rl = (RelativeLayout) findViewById(R.id.animation_person_rl);
        anim_gift = (ImageView) findViewById(R.id.animation_gift);
        anim_num = (TextView) findViewById(R.id.animation_num);
        anim_header = (ImageView) findViewById(R.id.gift_userheader_iv);
        anim_nickname = (TextView) findViewById(R.id.gift_usernickname_tv);
        anim_sign = (TextView) findViewById(R.id.gift_usersign_tv);
    }

    public boolean isShowing()
    {
        return isShowing;
    }

    // 起始影藏view
    public void hideView()
    {
        anim_gift.setVisibility(INVISIBLE);
        anim_num.setVisibility(INVISIBLE);
    }

    public void setModel(GiftModel model)
    {
        if (0 != model.giftCount)
        {
            this.repeatCount = model.giftCount;
        }
        if (!TextUtils.isEmpty(model.nickname))
        {
            anim_nickname.setText(model.nickname);
        }
        if (!TextUtils.isEmpty(model.sig))
        {
            anim_sign.setText(model.sig);
        }
    }

    // 创建动画
    public AnimatorSet createAnimation(int repeatCount)
    {
        hideView();
        // 布局飞入
        ObjectAnimator flyFromLtoR = GiftAnimationUtil.createFlyFromLtoR(anim_rl, -getWidth(), 0, 400,
                new OvershootInterpolator());
        flyFromLtoR.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                GiftLayout.this.setVisibility(View.VISIBLE);
                GiftLayout.this.setAlpha(1f);
                isShowing = true;
                anim_num.setText("x " + 1);
            }
        });
        // 礼物飞入
        ObjectAnimator flyFromLtoR2 = GiftAnimationUtil.createFlyFromLtoR(anim_gift, -getWidth(), 0, 400,
                new DecelerateInterpolator());
        flyFromLtoR2.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                anim_gift.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                // GiftAnimationUtil.startAnimationDrawable(anim_light);
                anim_num.setVisibility(View.VISIBLE);
            }
        });
        // 数量增加
        ObjectAnimator scaleGiftNum = GiftAnimationUtil.scaleGiftNum(anim_num, repeatCount);
        scaleGiftNum.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationRepeat(Animator animation)
            {
                anim_num.setText("x " + (++starNum));
            }
        });
        // 向上渐变消失
        ObjectAnimator fadeAnimator = GiftAnimationUtil.createFadeAnimator(GiftLayout.this, 0, -100, 300, 400);
        fadeAnimator.addListener(new AnimatorListenerAdapter()
        {

            @Override
            public void onAnimationEnd(Animator animation)
            {
                GiftLayout.this.setVisibility(View.INVISIBLE);
            }
        });
        // 复原
        ObjectAnimator fadeAnimator2 = GiftAnimationUtil.createFadeAnimator(GiftLayout.this, 100, 0, 20, 0);

        AnimatorSet animatorSet = GiftAnimationUtil.startAnimation(flyFromLtoR, flyFromLtoR2, scaleGiftNum,
                fadeAnimator, fadeAnimator2);
        animatorSet.addListener(new AnimatorListenerAdapter()
        {

            @Override
            public void onAnimationEnd(Animator animation)
            {
                starNum = 1;
                isShowing = false;
            }

        });
        return animatorSet;
    }

}
