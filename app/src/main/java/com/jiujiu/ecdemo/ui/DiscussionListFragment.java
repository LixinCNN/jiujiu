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
package com.jiujiu.ecdemo.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiujiu.ecdemo.common.dialog.ECProgressDialog;
import com.jiujiu.ecdemo.common.utils.DemoUtils;
import com.jiujiu.ecdemo.common.utils.ECPreferences;
import com.jiujiu.ecdemo.common.utils.LogUtil;
import com.jiujiu.ecdemo.common.view.SettingItem;
import com.jiujiu.ecdemo.core.ClientUser;
import com.jiujiu.ecdemo.ui.chatting.IMChattingHelper;
import com.jiujiu.ecdemo.ui.chatting.base.EmojiconTextView;
import com.jiujiu.ecdemo.ui.contact.ContactLogic;
import com.jiujiu.ecdemo.ui.contact.ECContacts;
import com.jiujiu.ecdemo.ui.settings.SettingPersionInfoActivity;
import com.jiujiu.ecdemo.common.utils.ECPreferenceSettings;
import com.jiujiu.ecdemo.storage.ContactSqlManager;
import com.jiujiu.ecdemo.ui.settings.EditConfigureActivity;

import java.io.InvalidClassException;

/**
 * 讨论组列表界面
 */
public class DiscussionListFragment extends TabFragment implements
		View.OnClickListener{
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
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(com.jiujiu.ecdemo.R.layout.settings_activity, container, false);

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mPhotoView = (ImageView) view.findViewById(com.jiujiu.ecdemo.R.id.desc);
		mUsername = (EmojiconTextView) view.findViewById(com.jiujiu.ecdemo.R.id.contact_nameTv);
		mNumber = (TextView) view.findViewById(com.jiujiu.ecdemo.R.id.contact_numer);
		mSignureTv = (TextView) view.findViewById(com.jiujiu.ecdemo.R.id.contact_signure);

		mSignureTv.setText(CCPAppManager.getClientUser().getSignature());

		mSettingSound = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_new_msg_sound);
		mSettingSound.getCheckedTextView().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateNewMsgNotification(0);
			}
		});
		mSettingShake = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_new_msg_shake);
		mSettingShake.getCheckedTextView().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateNewMsgNotification(1);
			}
		});
		mSettingExit = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.setting_exit);
		mSettingExit.setVisibility(View.GONE);
//		mSettingExit.setOnClickListener(mSettingExitClickListener);
		mSettingSwitch = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.setting_switch);
		mSettingSwitch.setVisibility(View.GONE);
//		mSettingSwitch.setOnClickListener(mSettingSwitchClickListener);

		mSettingAbout = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_about);
		mSettingSuggest = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_suggest);
		mSettingRatio = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_ratio);
		mSettingAbout.setOnClickListener(this);
		mSettingRatio.setOnClickListener(this);
		mSettingSuggest.setOnClickListener(this);

		mSettingServerIp = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_serverIP);
		mSettingAppkey = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_appkey);
		mSettingToken = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_token);
		mSettingUpdater = (SettingItem) view.findViewById(com.jiujiu.ecdemo.R.id.settings_update);
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
				}
			});
		} else {
			mSettingUpdater.setNewUpdateVisibility(false);
		}
		initConfigValue();
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
	private TextView mSignureTv;

	private final class OnConfigClickListener implements View.OnClickListener {

		private int type;
		public OnConfigClickListener(int type) {
			this.type = type;
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity() , EditConfigureActivity.class);
			intent.putExtra("setting_type" , type);
			startActivityForResult(intent , 0xa);
		}
	}
	/**
	 * 处理退出操作
	 */
	private void handleLogout(boolean isNotice) {
		mPostingdialog = new ECProgressDialog(getActivity(), com.jiujiu.ecdemo.R.string.posting_logout);
		mPostingdialog.show();
		SDKCoreHelper.logout(isNotice);
		if (!isNotice){
			try {
				Intent outIntent = new Intent(getActivity(), LauncherActivity.class);
				outIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, "", true);
				startActivity(outIntent);
				getActivity().finish();
			} catch (InvalidClassException e) {
				e.printStackTrace();
			}
		} else{
			try {
				Intent outIntent = new Intent(getActivity(), LauncherActivity.class);
				outIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT, true, true);
				startActivity(outIntent);
				finish();
				return ;
			} catch (InvalidClassException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onResume() {
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
	@Override
	public void onPause() {
		super.onPause();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//			case R.id.btn_left:
//				hideSoftKeyboard();
//				finish();
//				break;
			case com.jiujiu.ecdemo.R.id.text_right://可更改为界面内部的按钮
				startActivity(new Intent(getActivity() , SettingPersionInfoActivity.class));
				break;
			case com.jiujiu.ecdemo.R.id.settings_about:
				break;
			case com.jiujiu.ecdemo.R.id.settings_ratio:
				break;
			case com.jiujiu.ecdemo.R.id.settings_suggest:
				break;
			default:
				break;
		}
	}
	protected boolean isEnableSwipe() {
		return true;
	}
	private boolean isRefresh=false;
	public void onDisGroupFragmentVisible(boolean visible) {

		if(visible&&isVisible()&&!isRefresh){
			onSyncGroup();
		}
	}
	public void onSyncGroup() {
		isRefresh=true;
	}

	@Override
	protected void onReleaseTabUI() {
		// TODO Auto-generated method stub

	}
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return com.jiujiu.ecdemo.R.layout.settings_activity;
	}
	@Override
	protected void onTabFragmentClick() {
		// TODO Auto-generated method stub

	}
}
