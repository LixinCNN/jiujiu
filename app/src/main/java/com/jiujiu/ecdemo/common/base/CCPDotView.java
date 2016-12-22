package com.jiujiu.ecdemo.common.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jiujiu.ecdemo.common.utils.LogUtil;


public class CCPDotView extends LinearLayout {

	private int defaultCount = 9;
	private int defaultNormal = com.jiujiu.ecdemo.R.drawable.page_normal;
	private int defaultActive = com.jiujiu.ecdemo.R.drawable.page_active;

	/**
	 * @param context
	 */
	public CCPDotView(Context context) {
		this(context , null);
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public CCPDotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public CCPDotView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	
	private void init(Context context, AttributeSet attrs) {
		TypedArray typedArray = context.obtainStyledAttributes(com.jiujiu.ecdemo.R.styleable.AppPanel);
		int count = typedArray.getResourceId(com.jiujiu.ecdemo.R.styleable.AppPanel_dot_count, 0);
		typedArray.recycle();
		setDotCount(count);
	}

	public void setMaxCount(int count) {
		
		LogUtil.d(LogUtil.getLogUtilsTag(CCPDotView.class), "CCPDotView.setMaxCount: " + count);
		this.defaultCount = count;
	}
	
	public void initDotResource() {
		defaultNormal = com.jiujiu.ecdemo.R.drawable.page_normal_dark;
		defaultActive = com.jiujiu.ecdemo.R.drawable.page_active_dark;
	}
	
	/**
	 * The total number of dot, namely the total number of pages
	 * @param count
	 */
	public void setDotCount(int count) {
		
		LogUtil.d(LogUtil.getLogUtilsTag(CCPDotView.class), "CCPDotView.setDotCount: " + count);
		
		if(count < 0) {
			
			return ;
		}
		
		if(count > this.defaultCount) {
			LogUtil.d(LogUtil.getLogUtilsTag(CCPDotView.class), "setDotCount large than max count :" + this.defaultCount);
			count = defaultCount;
		}
		
		removeAllViews();
		
		while(count != 0 ) {
			ImageView imageControl = (ImageView) View.inflate(getContext(), com.jiujiu.ecdemo.R.layout.ccppage_control_image, null);
			
			// drak_dot
			imageControl.setImageResource(defaultNormal);
			addView(imageControl);
			
			count --;
		}
		
		// white_dot
		ImageView imageView = (ImageView) getChildAt(0);
		if(imageView != null) {
			imageView.setImageResource(defaultActive);
		}
	}
 	
	
	/**
	 * Set the current sdotselected
	 * @param selecteDot
	 */
	public void setSelectedDot(int selecteDot) {

		LogUtil.d(LogUtil.getLogUtilsTag(CCPDotView.class), "setSelectedDot:target index is : "
				+ selecteDot);

		if (selecteDot >= getChildCount()) {

			selecteDot = getChildCount() - 1;

		}
		LogUtil.d(LogUtil.getLogUtilsTag(CCPDotView.class),
				"setSelectedDot:after adjust index is : " + selecteDot);
		for (int i = 0; i < getChildCount(); i++) {
			// drak_dot
			((ImageView) getChildAt(i)).setImageResource(defaultNormal);
		}
		if (selecteDot < 0) {
			selecteDot = 0;
		}

		// white_dot
		ImageView localImageView = (ImageView) getChildAt(selecteDot);
		if (localImageView != null) {
			localImageView.setImageResource(defaultActive);
		}
	}

	public int getDotCount() {
		return defaultCount;
	}

	
}

