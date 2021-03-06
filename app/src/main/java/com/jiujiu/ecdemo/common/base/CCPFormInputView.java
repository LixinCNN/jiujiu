package com.jiujiu.ecdemo.common.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujiu.ecdemo.common.utils.LogUtil;

public class CCPFormInputView extends LinearLayout {

	public static final String TAG = "ECDemo.CCPFormInputView";

	private Context mContext;
	private TextView mTitleView;
	private EditText mContentEditText;
	private int mLayout = -1;
	private CharSequence mTitle;
	private CharSequence mHint;

	private OnFocusChangeListener mOnFocusChangeListener;

	/**
	 * @param context
	 */
	public CCPFormInputView(Context context) {
		super(context);
		mContext = null;
		mOnFocusChangeListener = null;
		mLayout = -1;
		mContext = context;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CCPFormInputView(Context context, AttributeSet attrs) {
		this(context, attrs , -1);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	@TargetApi(VERSION_CODES.HONEYCOMB)
	public CCPFormInputView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mContext = null;
		mLayout = -1;
		mOnFocusChangeListener = null;

		if(isInEditMode()) {
			return ;
		}
		TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, com.jiujiu.ecdemo.R.styleable.form_input, defStyle, 0);
		mTitle = obtainStyledAttributes.getString(com.jiujiu.ecdemo.R.styleable.form_input_form_title);
		mHint = obtainStyledAttributes.getString(com.jiujiu.ecdemo.R.styleable.form_input_form_hint);
		mLayout = obtainStyledAttributes.getResourceId(com.jiujiu.ecdemo.R.styleable.form_input_form_layout , mLayout);
		obtainStyledAttributes.recycle();
		inflate(context, mLayout, this);
		mContext = context;
	}

	/**
	 *
	 * @param l
	 */
	public void setOnFormInputViewFocusChangeListener(OnFocusChangeListener l) {
		mOnFocusChangeListener = l;
	}

	public void setFormInputViewImeOptions() {
		if(mContentEditText != null) {
			mContentEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
			return ;
		}
		LogUtil.e(TAG, "mContentEditText is null");
	}

	public void setFormInputViewInputTypeForPhone() {
		if(mContentEditText != null) {
			mContentEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
			return ;
		}
		LogUtil.e(TAG, "mContentEditText is null");
	}

	public EditText getFormInputEditView() {
		return mContentEditText;
	}

	/**
	 *
	 * @param textWatcher
	 */
	public void addTextChangedListener(TextWatcher textWatcher) {

		if(mContentEditText != null) {
			mContentEditText.addTextChangedListener(textWatcher);
			return ;
		}
		LogUtil.w(TAG, "watcher : " + textWatcher + " ,mContentEditText : " + mContentEditText);
	}

	/**
	 *
	 * @return
	 */
	public Editable getText() {
		if(mContentEditText != null) {
			return mContentEditText.getText();
		}

		LogUtil.e(TAG, "mContentEditText is null");
		return null;
	}

	@Override
	public boolean isInEditMode() {
		return super.isInEditMode();
	}

	/**
	 *
	 * @param text
	 */
	public void setText(CharSequence text) {
		if(mContentEditText != null) {
			mContentEditText.setText(text);
		}

		LogUtil.e(TAG, "mContentEditText is null");
	}

	public void setInputTitle(CharSequence text) {
		if(mTitleView != null) {
			mTitleView.setText(text);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mTitleView = (TextView) findViewById(com.jiujiu.ecdemo.R.id.title);
		mContentEditText = (EditText) findViewById(com.jiujiu.ecdemo.R.id.edittext);

		if(mTitleView == null || mContentEditText == null) {
			if(!isInEditMode()) {
				LogUtil.w(TAG, "mTitleView: " + mTitleView + " ,mContentEditText: " + mContentEditText);
			}
		} else {
			if (mContentEditText != null ) {
				mContentEditText.setOnFocusChangeListener(new  OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {

						if(v == mContentEditText) {
							if(hasFocus) {
								setBackgroundResource(com.jiujiu.ecdemo.R.drawable.input_bar_bg_active);
							} else {
								setBackgroundResource(com.jiujiu.ecdemo.R.drawable.input_bar_bg_normal);
							}

							if(mOnFocusChangeListener != null) {
								mOnFocusChangeListener.onFocusChange(v, hasFocus);
							}
						}
					}
				});
			}

			if(mTitle != null) {
				mTitleView.setText(mTitle);
			}

			if(mHint != null) {
				mContentEditText.setHint(mHint);
			}
		}


	}

}
