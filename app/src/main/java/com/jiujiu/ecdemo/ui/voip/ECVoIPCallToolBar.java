package com.jiujiu.ecdemo.ui.voip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


public class ECVoIPCallToolBar extends LinearLayout {
    public ECVoIPCallToolBar(Context context) {
        this(context, null);
    }

    public ECVoIPCallToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECVoIPCallToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        View.inflate(getContext(), com.jiujiu.ecdemo.R.layout.ec_voip_toolbar, this);
    }
}
