/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jiujiu.ecdemo.ui.group;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupProfileView extends LinearLayout {

	/**群组名称*/
	private TextView mGroupNameView;
	/**群组创建者*/
	private TextView mGroupOwnerView;
	/**群组id*/
	private TextView mGroupIdView;
	
	/**
	 * @param context
	 */
	public GroupProfileView(Context context) {
		super(context);
		initGroupProfileView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public GroupProfileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initGroupProfileView();
	}

	/**
	 * 
	 */
	private void initGroupProfileView() {
		View.inflate(getContext(), com.jiujiu.ecdemo.R.layout.group_profile, this);
		
		mGroupNameView = (TextView) findView(com.jiujiu.ecdemo.R.id.group_name);
		mGroupOwnerView = (TextView) findView(com.jiujiu.ecdemo.R.id.group_owner);
		mGroupIdView = (TextView) findView(com.jiujiu.ecdemo.R.id.group_id);
	}
	
	/**
	 * 设置群组名称
	 * @param text
	 */
	public final void setNameText(CharSequence text) {
		mGroupNameView.setText(text);
	}
	
	/**
	 * 设置群组名称
	 * @param resid
	 */
	public final void setNameText(int resid) {
		setNameText(getContext().getResources().getText(resid));
    }
	
	/**
	 * 设置群组创建者
	 * @param text
	 */
	public final void setOwnerText(CharSequence text) {
		mGroupOwnerView.setText(text);
	}
	
	/**
	 * 设置群组ID
	 * @param text
	 */
	public final void setGroupIdText(CharSequence text) {
		mGroupIdView.setText(text);
    }
	
	/**
	 * 查找布局
	 * @param resid
	 * @return
	 */
	private View findView(int resid) {
		return findViewById(resid).findViewById(android.R.id.summary);
	}
}
