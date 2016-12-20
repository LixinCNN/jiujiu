package com.jiujiu.ecdemo.ui.meeting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;

import com.jiujiu.ecdemo.common.base.CCPFormInputView;
import com.jiujiu.ecdemo.common.base.SpinnerSelectView;
import com.jiujiu.ecdemo.common.utils.ToastUtil;
import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.yuntongxun.ecsdk.ECMeetingManager;
/*
* 创建语音群聊房间
*/

public class CreateVoiceMeetingActivity extends ECSuperActivity
        implements View.OnClickListener{

    private static final String TAG = "ECSDK.Demo.CreateVoiceMeetingActivity";
    public static final String EXTRA_MEETING_PARAMS = "com.yuntongxun.meeting_params";
    /**语音群聊房间名称输入控件*/
    private CCPFormInputView mNameFormInputView;
    /**语音群聊房间名称输入输入框*/
    private EditText mNameEditView;
    /**语音群聊房间密码输入控件*/
    private CCPFormInputView mPasswordFormInputView;
    /**语音群聊房间密码输入输入框*/
    private EditText mPasswordEditView;
    /**退出是否自动解散会议*/
    private CheckedTextView mCloseCheckedView;
    /**创建成功后是否自动加入会议*/
    private CheckedTextView mJoinCheckedView;
    /**会议无成员是否自动删除会议*/
    private CheckedTextView mDelCheckedView;
    /**会议房间提示音类型*/
    private SpinnerSelectView mToneTypeView;

    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.create_voice_meeting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt,
                com.jiujiu.ecdemo.R.drawable.btn_style_green, null,
                getString(com.jiujiu.ecdemo.R.string.app_create),
                getString(com.jiujiu.ecdemo.R.string.app_title_chatroom_create), null, this);
        initView();
    }

    /**
     * 初始化界面资源
     */
    private void initView() {
        mNameFormInputView = (CCPFormInputView) findViewById(com.jiujiu.ecdemo.R.id.meeting_name);
        mNameEditView = mNameFormInputView.getFormInputEditView();
        mNameEditView.requestFocus();
        mPasswordFormInputView = (CCPFormInputView) findViewById(com.jiujiu.ecdemo.R.id.meeting_pass);
        mPasswordEditView = mPasswordFormInputView.getFormInputEditView();
        mCloseCheckedView = (CheckedTextView) findViewById(com.jiujiu.ecdemo.R.id.auto_close);
        mCloseCheckedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseCheckedView.toggle();
            }
        });
        mJoinCheckedView = (CheckedTextView) findViewById(com.jiujiu.ecdemo.R.id.auto_join);
        mJoinCheckedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJoinCheckedView.toggle();
            }
        });
        mDelCheckedView = (CheckedTextView) findViewById(com.jiujiu.ecdemo.R.id.auto_del);
        mDelCheckedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelCheckedView.toggle();
            }
        });

        mToneTypeView = (SpinnerSelectView) findViewById(com.jiujiu.ecdemo.R.id.spinner_tone_type);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case com.jiujiu.ecdemo.R.id.text_right:
            	
            	
            	String meetingName=mNameEditView.getText().toString().trim();
            	if(TextUtils.isEmpty(meetingName)){
            		
            		ToastUtil.showMessage(com.jiujiu.ecdemo.R.string.create_meetingName_Null);
            		return;
            	}
            	
                ECMeetingManager.ECCreateMeetingParams.Builder builder = new ECMeetingManager.ECCreateMeetingParams.Builder();
                // 设置语音会议房间名称
                builder.setMeetingName(mNameEditView.getText().toString().trim())
                // 设置语音会议房间加入密码
                .setMeetingPwd(mPasswordEditView.getText().toString().trim())
                // 设置语音会议创建者退出是否自动解散会议
                .setIsAutoClose(mCloseCheckedView.isChecked())
                // 设置语音会议创建成功是否自动加入
                .setIsAutoJoin(mJoinCheckedView.isChecked())
                // 设置语音会议背景音模式
                .setVoiceMod(ECMeetingManager.ECCreateMeetingParams.ToneMode.values()[mToneTypeView.getChoiceItemPosition()])
                // 设置语音会议所有成员退出后是否自动删除会议
                .setIsAutoDelete(mDelCheckedView.isChecked());

                Intent intent = new Intent();
                intent.putExtra(EXTRA_MEETING_PARAMS , builder.create());
                setResult(RESULT_OK , intent);
                finish();
                break;
        }
    }
}
