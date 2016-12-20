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
 */package com.jiujiu.ecdemo.common;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import com.jiujiu.ecdemo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CCPAccessibilityManager {
    private Context mContext;

    private AccessibilityManager mAccessibilityManager;

    public static CCPAccessibilityManager mInstance;

    public static CCPAccessibilityManager getInstance() {
        if(mInstance == null) {
            mInstance = new CCPAccessibilityManager(CCPAppManager.getContext());
        }
        return mInstance;
    }

    private CCPAccessibilityManager(Context context) {
        mContext = context;
        mAccessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    public final void buildViewDesc(View view , String text , String desc , int index) {
        if(!mAccessibilityManager.isEnabled()) {
            return;
        }

        if(view == null || TextUtils.isEmpty(text) || mContext == null) {
            return;
        }

        Tool tool = new Tool();
        tool.addViewDesc(text);

        if(!TextUtils.isEmpty(desc)) {
            try {
                int parseInt = Integer.parseInt(desc);
                if(parseInt <= 0) {
                    return;
                }

                Resources resources = mContext.getResources();
                tool.addViewDesc(resources.getQuantityString(R.plurals.tab_desc_unread, 1, parseInt));

                String quantityString = resources.getQuantityString(R.plurals.tab_name_site_desc, 5, 3);
                tool.addViewDesc(quantityString + ( index + 1));
                tool.setViewContentDescription(view);
            } catch (Exception e) {
            }
        }
    }


    public class Tool {
        public static final String TAG = "Accessibility.Tool";

        private List<String> mList = new ArrayList<String>();

        public final void setViewContentDescription(View view) {

            if(mList == null || mList.isEmpty()) {
                return;
            }
            Iterator<String> iterator = mList.iterator();
            StringBuffer str = new StringBuffer();
            while (iterator.hasNext()) {
                if(str.length() > 0) {
                    str.append(",");
                }
                str.append(iterator.next());

            }
            view.setContentDescription(str.toString());
        }

        public final Tool addViewDesc(String desc) {
            mList.add(desc);
            return this;
        }
    }
}
