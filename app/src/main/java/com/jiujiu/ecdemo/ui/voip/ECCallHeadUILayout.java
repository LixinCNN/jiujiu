package com.jiujiu.ecdemo.ui.voip;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiujiu.ecdemo.storage.ContactSqlManager;
import com.jiujiu.ecdemo.ui.contact.ContactLogic;
import com.jiujiu.ecdemo.ui.contact.ECContacts;

/**
 * 呼叫界面顶部呼叫信息显示区域
 */
public class ECCallHeadUILayout extends LinearLayout implements View.OnClickListener {

    /**通话者昵称*/
    private TextView mCallName;
    /**通话号码*/
    private TextView mCallNumber;
    /**通话时间*/
    private Chronometer mCallTime;
    /**呼叫状态描述*/
    private TextView mCallMsg;
    /**头像*/
    private ImageView mPhotoView;
    /**当前是否正在进行通话*/
    private boolean mCalling = false;
    /**是否显示通话参数信息*/
    private boolean mShowCallTips = false;
    
    public LinearLayout daiLayout;
	private EditText mDmfInput;

    public ECCallHeadUILayout(Context context) {
        this(context , null);
    }

    public ECCallHeadUILayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ECCallHeadUILayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        View.inflate(getContext(), com.jiujiu.ecdemo.R.layout.ec_call_head_layout, this);
        mCallName = (TextView) findViewById(com.jiujiu.ecdemo.R.id.layout_call_name);
        mCallNumber = (TextView) findViewById(com.jiujiu.ecdemo.R.id.layout_call_number);
        mCallTime = (Chronometer) findViewById(com.jiujiu.ecdemo.R.id.chronometer);
        mCallMsg = (TextView) findViewById(com.jiujiu.ecdemo.R.id.layout_call_msg);
        mPhotoView = (ImageView) findViewById(com.jiujiu.ecdemo.R.id.layout_call_photo);
        daiLayout=(LinearLayout) findViewById(com.jiujiu.ecdemo.R.id.layout_dial_panel);
        
        setupKeypad();
        
        mDmfInput=(EditText) findViewById(com.jiujiu.ecdemo.R.id.dial_input_numer_TXT);
        
    }
    
   

    /**
     * 设置当前的呼叫状态
     * @param calling
     */
    public void setCalling(boolean calling) {
        this.mCalling = calling;

        if(calling) {
            mCallTime.setBase(SystemClock.elapsedRealtime());
            mCallTime.setVisibility(View.VISIBLE);
            mCallTime.start();
        } else {
            mCallTime.stop();
            mCallTime.setVisibility(View.GONE);
        }
        mCallMsg.setVisibility((calling && !mShowCallTips)? View.GONE:View.VISIBLE);
    }

    /**
     * 是否显示通话参数信息
     * @param isShowing
     */
    public void setCallTipsShowing(boolean isShowing) {
        mShowCallTips = isShowing;
    }

    /**
     * 设置呼叫昵称
     * @param text 昵称
     */
    public void setCallName(CharSequence text) {
        if(mCallName != null) {
            mCallName.setText(text);
        }
    }

    /**
     * 设置呼叫号码
     * @param mobile 号码
     */
    public void setCallNumber(CharSequence mobile) {
        if(mCallNumber != null) {
            mCallNumber.setText(mobile);

            if(mobile != null) {
                ECContacts mContacts = ContactSqlManager.getContact(mobile.toString());
                if(mContacts != null && mContacts.getRemark() != null && !"personal_center_default_avatar.png".equals(mContacts.getRemark())) {
                    mPhotoView.setImageBitmap(ContactLogic.getPhoto(mContacts.getRemark()));
                }
            }
        }
    }

    /**
     * 设置呼叫状态描述
     * @param text
     */
    public void setCallTextMsg(String text) {
        if(mCallMsg == null) {
            return ;
        }
        if((text == null || text.length() <= 0) && !mCalling ) {
            mCallMsg.setVisibility(View.GONE);
        } else {
            mCallMsg.setText(text);
            mCallMsg.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置呼叫状态描述
     * @param resid
     */
    public void setCallTextMsg(int resid) {
        setCallTextMsg(getResources().getString(resid));
    }
    
    private boolean isShowDial=false;
    public void controllerDiaNumUI(){
    	
    	if(!isShowDial){
    		mPhotoView.setVisibility(View.GONE);
    		daiLayout.setVisibility(View.VISIBLE);
    		isShowDial=true;
    	}else {
    		mPhotoView.setVisibility(View.VISIBLE);
    		daiLayout.setVisibility(View.GONE);
    		isShowDial=false;
    	}
    	
    }
    private void setupKeypad() {
		/** Setup the listeners for the buttons */
		findViewById(com.jiujiu.ecdemo.R.id.zero).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.one).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.two).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.three).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.four).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.five).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.six).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.seven).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.eight).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.nine).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.star).setOnClickListener(this);
		findViewById(com.jiujiu.ecdemo.R.id.pound).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	case com.jiujiu.ecdemo.R.id.zero: {
		keyPressed(KeyEvent.KEYCODE_0);
		return;
	}
	case com.jiujiu.ecdemo.R.id.one: {
		keyPressed(KeyEvent.KEYCODE_1);
		return;
	}
	case com.jiujiu.ecdemo.R.id.two: {
		keyPressed(KeyEvent.KEYCODE_2);
		return;
	}
	case com.jiujiu.ecdemo.R.id.three: {
		keyPressed(KeyEvent.KEYCODE_3);
		return;
	}
	case com.jiujiu.ecdemo.R.id.four: {
		keyPressed(KeyEvent.KEYCODE_4);
		return;
	}
	case com.jiujiu.ecdemo.R.id.five: {
		keyPressed(KeyEvent.KEYCODE_5);
		return;
	}
	case com.jiujiu.ecdemo.R.id.six: {
		keyPressed(KeyEvent.KEYCODE_6);
		return;
	}
	case com.jiujiu.ecdemo.R.id.seven: {
		keyPressed(KeyEvent.KEYCODE_7);
		return;
	}
	case com.jiujiu.ecdemo.R.id.eight: {
		keyPressed(KeyEvent.KEYCODE_8);
		return;
	}
	case com.jiujiu.ecdemo.R.id.nine: {
		keyPressed(KeyEvent.KEYCODE_9);
		return;
	}
	case com.jiujiu.ecdemo.R.id.star: {
		keyPressed(KeyEvent.KEYCODE_STAR);
		return;
	}
	case com.jiujiu.ecdemo.R.id.pound: {
		keyPressed(KeyEvent.KEYCODE_POUND);
		return;
	}
	}
	}
	 void keyPressed(int keyCode) {
			
			KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
			mDmfInput.getText().clear();
			mDmfInput.onKeyDown(keyCode, event);
			if(sendDTMFDelegate!=null){
			sendDTMFDelegate.sendDTMF(mDmfInput.getText().toString().toCharArray()[0]);
			}
		}
	 
	 public interface OnSendDTMFDelegate {
	        void sendDTMF(char c);
	    }
	 
	 private OnSendDTMFDelegate sendDTMFDelegate;

	public OnSendDTMFDelegate getSendDTMFDelegate() {
		return sendDTMFDelegate;
	}

	public void setSendDTMFDelegate(OnSendDTMFDelegate sendDTMFDelegate) {
		this.sendDTMFDelegate = sendDTMFDelegate;
	}
	 
	 

}
