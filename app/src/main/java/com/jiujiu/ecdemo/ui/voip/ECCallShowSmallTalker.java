package com.jiujiu.ecdemo.ui.voip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jiujiu.ecdemo.R;


public class ECCallShowSmallTalker extends LinearLayout {

    public ECCallShowSmallTalker(Context context) {
        this(context, null);
    }

    public ECCallShowSmallTalker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECCallShowSmallTalker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.ec_voip_call_show_small_talker, this);
    }
}
