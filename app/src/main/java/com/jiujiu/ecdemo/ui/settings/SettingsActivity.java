package com.jiujiu.ecdemo.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiujiu.ecdemo.common.CCPAppManager;
import com.jiujiu.ecdemo.common.dialog.ECAlertDialog;
import com.jiujiu.ecdemo.common.dialog.ECProgressDialog;
import com.jiujiu.ecdemo.common.utils.DemoUtils;
import com.jiujiu.ecdemo.common.utils.ECPreferenceSettings;
import com.jiujiu.ecdemo.common.utils.ECPreferences;
import com.jiujiu.ecdemo.common.utils.LogUtil;
import com.jiujiu.ecdemo.common.view.SettingItem;
import com.jiujiu.ecdemo.core.ClientUser;
import com.jiujiu.ecdemo.storage.ContactSqlManager;
import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.jiujiu.ecdemo.ui.LauncherActivity;
import com.jiujiu.ecdemo.ui.SDKCoreHelper;
import com.jiujiu.ecdemo.ui.chatting.IMChattingHelper;
import com.jiujiu.ecdemo.ui.chatting.base.EmojiconTextView;
import com.jiujiu.ecdemo.ui.contact.ContactLogic;
import com.jiujiu.ecdemo.ui.contact.ECContacts;

import java.io.InvalidClassException;

import utils.RedPacketUtil;


/**
 * 设置界面/设置新消息提醒（声音或者振动）
 */
public class SettingsActivity extends ECSuperActivity implements View.OnClickListener{

    private static final String TAG = "ECDemo.SettingsActivity";

    public static final int CONFIG_TYPE_SERVERIP = 1;
    public static final int CONFIG_TYPE_APPKEY = 2;
    public static final int CONFIG_TYPE_TOKEN = 3;
    public static final int CONFIG_TYPE_GROUP_NAME = 4;
    public static final int CONFIG_TYPE_GROUP_NOTICE = 5;
    /**头像*/
    private ImageView mPhotoView;
    /**号码*/
    private EmojiconTextView mUsername;
    /**昵称*/
    private TextView mNumber;
    private SettingItem mSettingSound;
    private SettingItem mSettingShake;
    private SettingItem mSettingServerIp;
    private SettingItem mSettingAppkey;
    private SettingItem mSettingToken;
    private SettingItem mSettingExit;
    private SettingItem mSettingSwitch;
    private SettingItem mSettingUpdater;
    private SettingItem mSettingAbout;
    private SettingItem mSettingSuggest;
    //零钱
    private SettingItem mSettingRatio;
    private SettingItem settings_money;
    private ECProgressDialog mPostingdialog;

    private int mExitType = 0;

    private final View.OnClickListener mSettingExitClickListener
                = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	View contentView = View.inflate(SettingsActivity.this, com.jiujiu.ecdemo.R.layout.exit_dialog_view , null);
            	final CheckBox cb = (CheckBox) contentView.findViewById(com.jiujiu.ecdemo.R.id.open_dialog_cb);
            	cb.setChecked(true);
            	ECAlertDialog alertDialog = new ECAlertDialog(SettingsActivity.this);
            	alertDialog.setContentView(contentView);
            	alertDialog.setButton(ECAlertDialog.BUTTON_NEGATIVE, com.jiujiu.ecdemo.R.string.app_cancel, null);
            	alertDialog.setButton(ECAlertDialog.BUTTON_POSITIVE, com.jiujiu.ecdemo.R.string.dialog_alert_close, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mExitType = 1;
			            handleLogout(cb.isChecked());
					}
				});
            	alertDialog.show();
        }
    };

    private final View.OnClickListener mSettingSwitchClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            ECAlertDialog buildAlert = ECAlertDialog.buildAlert(SettingsActivity.this, com.jiujiu.ecdemo.R.string.settings_logout_warning_tip, null, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mExitType = 0;
                    handleLogout(false);
                }

            });
            buildAlert.setTitle(com.jiujiu.ecdemo.R.string.settings_logout);
            buildAlert.show();
        }
    };

	private TextView mSignureTv;

    private final class OnConfigClickListener implements View.OnClickListener {

        private int type;
        public OnConfigClickListener(int type) {
            this.type = type;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingsActivity.this , EditConfigureActivity.class);
            intent.putExtra("setting_type" , type);
            startActivityForResult(intent , 0xa);
        }
    }

    /**
     * 处理退出操作
     */
    private void handleLogout(boolean isNotice) {
        mPostingdialog = new ECProgressDialog(this, com.jiujiu.ecdemo.R.string.posting_logout);
        mPostingdialog.show();
        SDKCoreHelper.logout(isNotice);
    }

    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.settings_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt,
                com.jiujiu.ecdemo.R.drawable.btn_style_green, null,
                getString(com.jiujiu.ecdemo.R.string.app_server_config),
                getString(com.jiujiu.ecdemo.R.string.app_set), null, this);

        registerReceiver(new String[]{SDKCoreHelper.ACTION_LOGOUT});
    }

    /**
     * 加载页面布局
     */
    private void initView() {
        mPhotoView = (ImageView) findViewById(com.jiujiu.ecdemo.R.id.desc);
        mUsername = (EmojiconTextView) findViewById(com.jiujiu.ecdemo.R.id.contact_nameTv);
        mNumber = (TextView) findViewById(com.jiujiu.ecdemo.R.id.contact_numer);
       mSignureTv = (TextView) findViewById(com.jiujiu.ecdemo.R.id.contact_signure);
        
        mSignureTv.setText(CCPAppManager.getClientUser().getSignature());

        mSettingSound = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_new_msg_sound);
        mSettingSound.getCheckedTextView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateNewMsgNotification(0);
            }
        });
        mSettingShake = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_new_msg_shake);
        mSettingShake.getCheckedTextView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateNewMsgNotification(1);
            }
        });
        mSettingExit = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.setting_exit);
        mSettingExit.setOnClickListener(mSettingExitClickListener);
        mSettingSwitch = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.setting_switch);
        mSettingSwitch.setOnClickListener(mSettingSwitchClickListener);

        mSettingAbout = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_about);
        mSettingSuggest = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_suggest);
        mSettingRatio = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_ratio);
        mSettingAbout.setOnClickListener(this);
        mSettingRatio.setOnClickListener(this);
        mSettingSuggest.setOnClickListener(this);

        mSettingServerIp = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_serverIP);
        mSettingAppkey = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_appkey);
        mSettingToken = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_token);
        mSettingUpdater = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_update);
        mSettingUpdater.setTitleText(getString(com.jiujiu.ecdemo.R.string.demo_current_version ,CCPAppManager.getVersion()));
        mSettingServerIp.setOnClickListener(new OnConfigClickListener(CONFIG_TYPE_SERVERIP));
        mSettingAppkey.setOnClickListener(new OnConfigClickListener(CONFIG_TYPE_APPKEY));
        mSettingToken.setOnClickListener(new OnConfigClickListener(CONFIG_TYPE_TOKEN));

        if( IMChattingHelper.getInstance() != null
                && SDKCoreHelper.mSoftUpdate != null
                && DemoUtils.checkUpdater(SDKCoreHelper.mSoftUpdate.version)) {
            mSettingUpdater.setNewUpdateVisibility(true);
            mSettingUpdater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CCPAppManager.startUpdater(SettingsActivity.this);
                }
            });
        } else {
            mSettingUpdater.setNewUpdateVisibility(false);
        }
        initConfigValue();
        settings_money = (SettingItem) this.findViewById(com.jiujiu.ecdemo.R.id.settings_money);
        settings_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fromNickname = CCPAppManager.getClientUser().getUserName();
                String fromAvatarUrl = "none";
                fromAvatarUrl = TextUtils.isEmpty(fromAvatarUrl) ? "none" : fromAvatarUrl;
                fromNickname = TextUtils.isEmpty(fromNickname) ? CCPAppManager.getClientUser().getUserId() : fromNickname;
                RedPacketUtil.startChangeActivity(SettingsActivity.this, fromNickname, fromAvatarUrl, CCPAppManager.getClientUser().getUserId());
            }
        });
    }

    private void initConfigValue() {
        // mSettingServerIp.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_SERVERIP));
        mSettingAppkey.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_APPKEY));
        mSettingToken.setDetailText(getConfig(ECPreferenceSettings.SETTINGS_TOKEN));
    }



    private String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId() , (String)settings.getDefaultValue());
        return value;
    }


    @Override
    protected void handleReceiver(Context context, Intent intent) {
        super.handleReceiver(context, intent);
        if(SDKCoreHelper.ACTION_LOGOUT.equals(intent.getAction())) {

            try {
                Intent outIntent = new Intent(SettingsActivity.this, LauncherActivity.class);
                outIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(mExitType == 1) {
                    ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT, true, true);
                    startActivity(outIntent);
                    finish();
                    return ;
                }
                dismissPostingDialog();
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, "", true);
                startActivity(outIntent);
                finish();
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSettings();
        initActivityState();
        if(mSignureTv!=null){
          mSignureTv.setText(CCPAppManager.getClientUser().getSignature());
        }
    }

    /**
     * 初始化
     */
    private void initSettings() {
        initNewMsgNotificationSound();
        initNewMsgNotificationShake();
    }

    /**
     * 初始化新消息声音设置参数
     */
    private void initNewMsgNotificationSound() {
        if(mSettingSound == null) {
            return ;
        }
        mSettingSound.setVisibility(View.VISIBLE);
        boolean shakeSetting = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getDefaultValue());
        mSettingSound.setChecked(shakeSetting);
    }

    /**
     * 初始化新消息震动设置参数
     */
    private void initNewMsgNotificationShake() {
        if(mSettingShake == null) {
            return ;
        }
        mSettingShake.setVisibility(View.VISIBLE);
        boolean shakeSetting = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_NEW_MSG_SHAKE.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_NEW_MSG_SHAKE.getDefaultValue());
        mSettingShake.setChecked(shakeSetting);
    }

    /**
     * 更新状态设置
     * @param type
     */
    protected void updateNewMsgNotification(int type) {
        try {
            if(type == 0) {
                if(mSettingSound == null) {
                    return ;
                }
                mSettingSound.toggle();
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND, mSettingSound.isChecked(), true);
                LogUtil.d(TAG, "com.yuntongxun.ecdemo_new_msg_sound " + mSettingSound.isChecked());
                return ;
            }
            if(type == 1) {
                if(mSettingShake == null) {
                    return ;
                }
                mSettingShake.toggle();
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_NEW_MSG_SHAKE, mSettingShake.isChecked(), true);
                LogUtil.d(TAG, "com.yuntongxun.ecdemo_new_msg_sound " + mSettingSound.isChecked());
            }
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置页面参数
     */
    private void initActivityState() {
        ClientUser clientUser = CCPAppManager.getClientUser();
        if(clientUser == null) {
            return ;
        }
        ECContacts contact = ContactSqlManager.getContact(clientUser.getUserId());
        if(contact == null) {
            return ;
        }

        mPhotoView.setImageBitmap(ContactLogic.getPhoto(contact.getRemark()));
        mUsername.setText(clientUser.getUserName());
        mNumber.setText(contact.getContactid());

    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if(mPostingdialog == null || !mPostingdialog.isShowing()) {
            return ;
        }
        mPostingdialog.dismiss();
        mPostingdialog = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case com.jiujiu.ecdemo.R.id.text_right:
                startActivity(new Intent(this , SettingPersionInfoActivity.class));
                break;
            case com.jiujiu.ecdemo.R.id.settings_about:
                startActivity(new Intent(this , AboutActivity.class));
                break;
            case com.jiujiu.ecdemo.R.id.settings_ratio:
                startActivity(new Intent(this , SetRatioActivity.class));
                break;
            case com.jiujiu.ecdemo.R.id.settings_suggest:
                startActivity(new Intent(this , SuggestActivity.class));
                break;
            default:
                break;
        }
    }

}
