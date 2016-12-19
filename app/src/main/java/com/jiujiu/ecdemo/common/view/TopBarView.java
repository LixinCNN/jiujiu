/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.jiujiu.ecdemo.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiujiu.ecdemo.R;
import com.jiujiu.ecdemo.common.utils.DensityUtil;
import com.jiujiu.ecdemo.ui.chatting.base.EmojiconTextView;

public class TopBarView extends LinearLayout {
    public static final int SHOW_SUTITLE = 2;
    private Context mContext ;
    private ImageView mLeftButton;
    private EmojiconTextView mMiddleButton;
    private TextView mMiddleSub;
    private ImageView mRightButton;
    private TextView mLeftText;
    private TextView mRightText;
    private OnClickListener mClickListener;

    private boolean mArrowUp = true;

    public TopBarView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        //setBackgroundResource(R.drawable.actionbar_bg);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.red_btn_color_normal)));
        LayoutInflater.from(getContext()).inflate(R.layout.common_view_top_bar, this, true);
        mLeftButton = (ImageView) findViewById(R.id.btn_left);
        mMiddleButton = (EmojiconTextView) findViewById(R.id.btn_middle);
        mMiddleSub = (TextView) findViewById(R.id.btn_middle_sub);
        mRightButton = (ImageView) findViewById(R.id.btn_right);
        mLeftText = (TextView) findViewById(R.id.text_left);
        mRightText = (TextView) findViewById(R.id.text_right);
    }

    private void setMiddleSubTitle(int type , String title , String subTitle , OnClickListener onClickListener) {
        if(type == 1) {
            setOnClickListener(onClickListener);
        }
        setTitle(title);
        if(TextUtils.isEmpty(subTitle) || type == 2) {
            mMiddleSub.setVisibility(View.GONE);
            return ;
        }
        mMiddleSub.setText(subTitle);
        mMiddleSub.setVisibility(View.VISIBLE);
        mMiddleSub.setOnClickListener(onClickListener);
    }

    /**
     * 显示正在加载Progressba
     */
    public void showTopProgressbar() {
        mRightButton.setVisibility(View.GONE);
        mRightText.setVisibility(View.GONE);
        ((RelativeLayout)findViewById(R.id.top_progressbar)).setVisibility(View.VISIBLE);
    }

    public void setRightButtonRes(int resId) {
        if(resId == -1) {
            mRightButton.setVisibility(View.GONE);
            return ;
        }
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.btn_topbar_paddingHorizontal);
        mRightButton.setImageResource(resId);
        mRightButton.setPadding(padding, 0, padding, 0);
    }

    public void setRightButtonText(String text) {
        if(text == null) {
            mRightText.setVisibility(View.GONE);
            return ;
        }
        mRightText.setText(text);
    }

    public void setTopbarUpdatePoint(boolean show) {
        View mTopbarUpdatePoint = findViewById(R.id.topbar_update_point);
        if(show) {
            mTopbarUpdatePoint.setVisibility(View.VISIBLE);
            return ;
        }
        mTopbarUpdatePoint.setVisibility(View.GONE);
    }

    public void setRightVisible() {
        mRightButton.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.VISIBLE);
        ((RelativeLayout)findViewById(R.id.top_progressbar)).setVisibility(View.GONE);
    }

    public void setTopbarRightPoint(boolean show) {
        View mTopbarRightPoint = findViewById(R.id.right_point);
        if(show) {
            mTopbarRightPoint.setVisibility(View.VISIBLE);
            return ;
        }
        mTopbarRightPoint.setVisibility(View.GONE);
    }

    public ImageView getLeftButton() {
        return mLeftButton;
    }

    public ImageView getRightButton() {
        return mRightButton;
    }

    public TextView getLeftText() {
        return mLeftText;
    }

    public TextView getRightText() {
        return mRightText;
    }

    public void setFront() {
        bringToFront();
    }

    public void setMiddleBtnArrowUp(boolean up , boolean arrow) {
        if(mArrowUp == up && !arrow) {
            return ;
        }

        mArrowUp = up;
        int id = R.drawable.common_top_bar_arrow_down;
        if(mArrowUp) {
            id = R.drawable.common_top_bar_arrow_up;
        }
        Drawable upDownDrawable = mContext.getResources().getDrawable(id);
        upDownDrawable.setBounds(0, 0, upDownDrawable.getIntrinsicWidth(), upDownDrawable.getIntrinsicHeight());
        mMiddleButton.setCompoundDrawablePadding(DensityUtil.dip2px(5.0F));
        mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, upDownDrawable, null);
    }

    public void setMiddleBtnPadding(int padding) {
        if(mMiddleButton == null) {
            return ;
        }
        mMiddleButton.setPadding(padding, 0, padding, 0);
    }

    public void setRightBtnEnable(boolean enabled) {
        mRightButton.setEnabled(enabled);
        mRightText.setEnabled(enabled);
    }

    public void setTitle(CharSequence title) {
        if(TextUtils.isEmpty(title)) {
            mMiddleButton.setVisibility(View.INVISIBLE);
            return ;
        }
        mMiddleButton.setText(title);
        mMiddleButton.setVisibility(View.VISIBLE);
        mMiddleButton.setOnClickListener(mClickListener);

        doSetTouchDelegate();
    }

    public void setTitle(String title) {
        if(TextUtils.isEmpty(title)) {
            mMiddleButton.setVisibility(View.INVISIBLE);
            return ;
        }
        mMiddleButton.setText(title);
        mMiddleButton.setVisibility(View.VISIBLE);
        mMiddleButton.setOnClickListener(mClickListener);

        doSetTouchDelegate();
    }

    // 设置标题的可点击范围
    private void doSetTouchDelegate() {
        final TextView middleBtn = mMiddleButton;
        post(new Runnable() {

            @Override
            public void run() {
                Rect rect = new Rect();
                rect.left = (middleBtn.getWidth() / 4);
                rect.right = (3 * middleBtn.getWidth() / 4);
                rect.top = 0;
                rect.bottom = middleBtn.getHeight();
                middleBtn.setTouchDelegate(new TouchDelegate(rect, /*TopBarView.this*/mMiddleSub));
            }
        });
    }

    public void setTitleDrawable(int resId) {
        if(resId == -1) {
            mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(resId), null, null, null);
            return ;
        }
        mMiddleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    public void setTopBarToStatus(int type, int leftResid, int rightResid,
                                  int titleRes, OnClickListener l) {
        String str = "";
        if (titleRes != -1) {
            str = getResources().getString(titleRes);
        }
        setTopBarToStatus(type, leftResid, rightResid, null, null, str, "", l);
    }

    public void setTopBarToStatus(int type, int leftResid, int rightResid,
                                  String title, OnClickListener l) {
        setTopBarToStatus(type, leftResid, rightResid, null, null, title, "", l);
    }

    public void setTopBarToStatus(int type, int leftResid, String rightText,
                                  String title, OnClickListener l) {
        setTopBarToStatus(type, leftResid, -1, null, rightText, title, "", l);
    }

    public  void setTopBarToStatus(int type, int leftResid, int rightResid, String leftText, String rightText, String title, String subTitle, OnClickListener l) {
        mClickListener = l;
        findViewById(R.id.common_top_wrapper).setOnClickListener(mClickListener);
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.btn_topbar_paddingHorizontal);
        if(leftResid <= 0 || leftText != null) {
            mLeftButton.setVisibility(View.GONE);
            if(leftText != null) {
                mLeftButton.setVisibility(View.GONE);
                mLeftText.setText(leftText);
                mLeftText.setVisibility(View.VISIBLE);
                mLeftText.setOnClickListener(l);
            } else {
                mLeftText.setVisibility(View.GONE);
            }

            if(leftResid > 0) {
                mLeftText.setBackgroundResource(leftResid);
                mLeftText.setPadding(padding, 0, padding, 0);
            }
        } else {
            mLeftButton.setImageResource(leftResid);
            mLeftButton.setPadding(padding, 0, padding, 0);
            mLeftButton.setVisibility(View.VISIBLE);
            mLeftButton.setOnClickListener(l);
        }

        if(rightResid <= 0 || rightText != null) {
            mRightButton.setVisibility(View.GONE);

            if(rightText != null) {
                mRightButton.setVisibility(View.GONE);
                mRightText.setText(rightText);
                mRightText.setVisibility(View.VISIBLE);
                mRightText.setOnClickListener(l);
            } else {
                mRightText.setVisibility(View.GONE);
            }

            if(rightResid > 0) {
                mRightText.setBackgroundResource(rightResid);
                mRightText.setPadding(padding, 0, padding, 0);
            }

        } else {
            mRightButton.setImageResource(rightResid);
            mRightButton.setPadding(padding, 0, padding, 0);
            mRightButton.setVisibility(View.VISIBLE);
            mRightButton.setOnClickListener(l);
        }

        setMiddleSubTitle(type, title, subTitle, l);
    }

	public EmojiconTextView getmMiddleButton() {
		return mMiddleButton;
	}

	
    
    
}
