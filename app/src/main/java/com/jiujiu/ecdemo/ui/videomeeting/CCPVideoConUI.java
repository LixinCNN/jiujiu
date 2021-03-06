
package com.jiujiu.ecdemo.ui.videomeeting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewDebug;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujiu.ecdemo.R;
import com.jiujiu.ecdemo.common.CCPAppManager;

import java.util.HashMap;


@SuppressLint("UseSparseArrays")
public class CCPVideoConUI extends LinearLayout {
	
	public static final int LAYOUT_KEY_MAIN_SURFACEVIEW = 0X1;
	public static final int LAYOUT_KEY_SUB_SURFACEVIEW = 0X2;
	public static final int LAYOUT_KEY_SUB_IMAGEVIEW_1 = 0X3;
	public static final int LAYOUT_KEY_SUB_IMAGEVIEW_2 = 0X4;
	public static final int LAYOUT_KEY_SUB_IMAGEVIEW_3 = 0X5;
	public static final int LAYOUT_KEY_SUB_IMAGEVIEW_4 = 0X6;

	public HashMap<Integer, View> videoLayoutCache = new HashMap<Integer, View>();
	private int mScreenWidth;
	private int mScreenHeight;
	
	private Context mContext;
	
	private SurfaceView mMainSurfaceView;
	
	private SurfaceView mSubSurfaceView;
	private FrameLayout mSubFrameLayout;
	
	private LayoutInflater mLayoutInflater;
	
	private OnVideoUIItemClickListener mVideoUIItemClickListener;
	

	private boolean isOperable;

    protected Object mTag;
    
    /**
     * Map used to store views' tags.
     */
    private SparseArray<Object> mKeyedTags;
	private Drawable drawableR;
	
	private int mVideoUIPadding = 0;
	private int mVideoUIMainKey = -1;
	
	public CCPVideoConUI(Context context) {
		super(context);
		initVideoUILayout(context);
	}

	public CCPVideoConUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		initVideoUILayout(context);
	}

	public CCPVideoConUI(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initVideoUILayout(context);
		
	}

	private void initVideoUILayout(Context context) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		int space = Math.round(6 * getResources().getDisplayMetrics().densityDpi / 160.0F);
		int subSurfaceViewWidth = Math.round((mScreenWidth - space*4)/3);
		int mainSurfaceViewWidth = subSurfaceViewWidth * 2 + space;
		
		mVideoUIPadding = Math.round(2 * getResources().getDisplayMetrics().densityDpi / 160.0F);
		
		LinearLayout topLayout = new LinearLayout(context);
		LayoutParams topLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT);
		topLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		topLayoutParams.weight = 1;
		topLayoutParams.bottomMargin = space;
		topLayout.setLayoutParams(topLayoutParams);

		// add main surfaceView;
		FrameLayout mainSurfaceView = getSurfaceViewLayout(space , LAYOUT_KEY_MAIN_SURFACEVIEW);
		topLayout.addView(mainSurfaceView);

		LinearLayout rTopLinearLayout = new LinearLayout(context);
		LayoutParams rfLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT);
		rfLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		rfLayoutParams.weight = 2;
		rfLayoutParams.leftMargin = space;
		rTopLinearLayout.setLayoutParams(rfLayoutParams);
		rTopLinearLayout.setOrientation(LinearLayout.VERTICAL);


		// --------------------------right 2 start -------------------------- 1
		// add right top two view (surfaceView and imageView)
		FrameLayout subSurfaceView = getSurfaceViewLayout(space , LAYOUT_KEY_SUB_SURFACEVIEW);
		mSubFrameLayout = subSurfaceView;
		rTopLinearLayout.addView(subSurfaceView);
		// ---------------------------------------------------- 2
		FrameLayout rImageView = getImageViewLayout(space , LAYOUT_KEY_SUB_IMAGEVIEW_1);
		rTopLinearLayout.addView(rImageView);
		// -----------------------------right 2 end --------------------

		topLayout.addView(rTopLinearLayout);



		// -------------------------------------bottom -------------------
		// add bottom three ImageView
		LinearLayout bottomLayout = new LinearLayout(context);
		LayoutParams bottomLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT);
		bottomLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		bottomLayoutParams.weight = 2;
		bottomLayoutParams.topMargin = space;
		bottomLayoutParams.bottomMargin = space;
		bottomLayout.setLayoutParams(bottomLayoutParams);
		// ---------------------------------------------------- 1
		FrameLayout bImageView2 = getImageViewLayout(space , LAYOUT_KEY_SUB_IMAGEVIEW_2);
		bottomLayout.addView(bImageView2);
		// ---------------------------------------------------- 1
		FrameLayout bImageView3 = getImageViewLayout(space , LAYOUT_KEY_SUB_IMAGEVIEW_3);
		bottomLayout.addView(bImageView3);
		// ---------------------------------------------------- 1
		FrameLayout bImageView4 = getImageViewLayout(space , LAYOUT_KEY_SUB_IMAGEVIEW_4);
		bottomLayout.addView(bImageView4);


		// add all view
		addView(topLayout);
		addView(bottomLayout);
	}

	private FrameLayout getImageViewLayout(int space , final int layoutKye) {
		FrameLayout fLayout = (FrameLayout) mLayoutInflater.inflate(R.layout.video_c_item, null);
		LayoutParams rfLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT);
		rfLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		rfLayoutParams.weight = 1;

		TextView textViewImage = (TextView) fLayout.findViewById(R.id.image);
		TextView textView = (TextView) fLayout.findViewById(R.id.text);

		if(layoutKye == LAYOUT_KEY_SUB_IMAGEVIEW_1) {
			rfLayoutParams.topMargin = space;
		} else if (layoutKye == LAYOUT_KEY_SUB_IMAGEVIEW_2) {
			rfLayoutParams.rightMargin = space;
		} else if (layoutKye == LAYOUT_KEY_SUB_IMAGEVIEW_3) {
			rfLayoutParams.rightMargin = space;
			rfLayoutParams.leftMargin = space;
		} else if (layoutKye == LAYOUT_KEY_SUB_IMAGEVIEW_4) {
			rfLayoutParams.leftMargin = space;
		}

		videoLayoutCache.put(layoutKye, fLayout);

		fLayout.setLayoutParams(rfLayoutParams);
		fLayout.setBackgroundResource(R.color.black);
		fLayout.getBackground().setAlpha(55);

		textView.getBackground().setAlpha(55);

		drawableR = getResources().getDrawable(R.drawable.three_point);
		drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());


		return fLayout;

	}

	private FrameLayout getSurfaceViewLayout(int space , final int layoutKye) {
		FrameLayout lframeLayout = (FrameLayout) mLayoutInflater.inflate(R.layout.video_c_local_surceview, null);
		LayoutParams fLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT
				, LayoutParams.MATCH_PARENT);
		fLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		fLayoutParams.weight = 1;
		
		SurfaceView surfaceView = (SurfaceView) lframeLayout.findViewById(R.id.surface_view);
		TextView textView = (TextView) lframeLayout.findViewById(R.id.text);
		
		surfaceView.setZOrderOnTop(false);
		
		if(layoutKye == LAYOUT_KEY_MAIN_SURFACEVIEW) {
			fLayoutParams.rightMargin = space;
			surfaceView.getHolder().setFixedSize(240, 320);
			mMainSurfaceView = surfaceView;
		} else if (layoutKye == LAYOUT_KEY_SUB_SURFACEVIEW) {
			fLayoutParams.bottomMargin = space;
			mSubSurfaceView = surfaceView;
		}
		
		// The video item stored in the cache
		videoLayoutCache.put(layoutKye, lframeLayout);
		
		lframeLayout.setLayoutParams(fLayoutParams);
		lframeLayout.setBackgroundResource(R.color.black);
		lframeLayout.getBackground().setAlpha(55);
		
		textView.getBackground().setAlpha(55);
		
		return lframeLayout;
		
	}
	
	void getScreenDisplayMetrics(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
	}
	
	public SurfaceView getMainSurfaceView() {
		return mMainSurfaceView;
	}
	
	public SurfaceView getSubSurfaceView() {
		return mSubSurfaceView;
	}
	

	public void setOperableEnable(boolean enabled) {
		isOperable = enabled;
	}
	

	public void setImageViewDrawable(int index , Drawable drawable) {
		View view = videoLayoutCache.get(index);
		String text = null;
		if(drawable == null) {
			text = "待加入";
			view.setBackgroundColor(mContext.getResources().getColor(R.color.black));
			view.setPadding(0, 0, 0, 0);
			view.getBackground().setAlpha(55);
		}
		
		setImageViewDrawableLoading(index, text, drawable);
	}
	

	public void setImageViewDrawableLoading(int index , String text , Drawable drawable) {
		View view = videoLayoutCache.get(index);
		if(view == null ){
			return;
		}
		TextView textView = (TextView)view.findViewById(R.id.image);
		textView.setBackgroundDrawable(drawable);
		textView.setText(text);
	}
	
	public void setImageViewBitmap(int index , Bitmap bitmap) {
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources() , bitmap);
		setImageViewDrawable(index, bitmapDrawable);
	}
	
	public void setVideoUIMainScreen(int index) {
		
		if(index <= 1) {
			return;
		}
		
		if(mVideoUIMainKey != -1) {
			// src.
			View viewMainSrc = videoLayoutCache.get(mVideoUIMainKey); 
			if(viewMainSrc != null) {
				viewMainSrc.setBackgroundColor(mContext.getResources().getColor(R.color.black));
				viewMainSrc.setPadding(0, 0, 0, 0);
				viewMainSrc.getBackground().setAlpha(55);
			}
		}
		
		// dest
		View view = videoLayoutCache.get(index);
		if(view != null ) {
			mVideoUIMainKey = index;
			view.setBackgroundColor(Color.WHITE);
			view.getBackground().setAlpha(255);
			view.setPadding(mVideoUIPadding, mVideoUIPadding, mVideoUIPadding, mVideoUIPadding);
		}
	}
	

	public void setVideoUIText(final int index , CharSequence textView) {
		
		setVideoUIText(index, textView, false);
		
	}
	

	public void setVideoUIText(final int index , CharSequence textView , boolean Operable) {
		
		View view = videoLayoutCache.get(index);
		TextView tempTextView = ((TextView)view.findViewById(R.id.text));
		tempTextView.setText(textView);
		
		if(textView == null 
				|| !Operable 
				|| index == LAYOUT_KEY_MAIN_SURFACEVIEW) {
			// If the operation is not allowed, do not set listener
			view.setOnClickListener(null);
			tempTextView.setCompoundDrawables(null, null, null, null);
			return;
		}
		
		tempTextView.setCompoundDrawables(null, null, drawableR, null);
		// Set the members item the click listener callback
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mVideoUIItemClickListener != null) {
					mVideoUIItemClickListener.onVideoUIItemClick(index);
				}
			}
		});
		
	}
	

	public String getVideoUIText(int index) {
		View view = videoLayoutCache.get(index);
		CharSequence text = ((TextView) view.findViewById(R.id.text)).getText();

		if (TextUtils.isEmpty(text)) {
			return null;
		}
		return text.toString();
	}
	
	public void setSubSurfaceView(SurfaceView surfaceView) {
		if(mSubFrameLayout != null) {
			if(mSubSurfaceView != null) {
				mSubFrameLayout.removeView(mSubSurfaceView);
			}
			surfaceView.setZOrderOnTop(false);
			mSubFrameLayout.addView(surfaceView,0);
		}
		
		setVideoUIText(LAYOUT_KEY_SUB_SURFACEVIEW, CCPAppManager.getUserId());//???
//		setVideoUIText(LAYOUT_KEY_SUB_SURFACEVIEW, CommomUtil.interceptStringOfIndex(CCPAppManager.getUserId(), 4));
	}
	
	public void setOnVideoUIItemClickListener(OnVideoUIItemClickListener l) {
		mVideoUIItemClickListener = l;
	}
	

    @ViewDebug.ExportedProperty
    public Object getTag() {
        return mTag;
    }


    public void setTag(final Object tag) {
        mTag = tag;
    }


    public Object getTag(int key) {
        if (mKeyedTags != null) return mKeyedTags.get(key);
        return null;
    }


    public void setTag(int key, final Object tag) {
        // If the package id is 0x00 or 0x01, it's either an undefined package
        // or a framework id
        if ((key >>> 24) < 2) {
            throw new IllegalArgumentException("The key must be an application-specific "
                    + "resource id.");
        }

        setKeyedTag(key, tag);
    }


    public void setTagInternal(int key, Object tag) {
        if ((key >>> 24) != 0x1) {
            throw new IllegalArgumentException("The key must be a framework-specific "
                    + "resource id.");
        }

        setKeyedTag(key, tag);
    }

    private void setKeyedTag(int key, Object tag) {
        if (mKeyedTags == null) {
            mKeyedTags = new SparseArray<Object>();
        }

        mKeyedTags.put(key, tag);
    }
    
    
    /**
     * 
     */
    public void release() {
    	
    }
	

	public interface OnVideoUIItemClickListener {
		

		void onVideoUIItemClick(int CCPUIKye);
	}
}
