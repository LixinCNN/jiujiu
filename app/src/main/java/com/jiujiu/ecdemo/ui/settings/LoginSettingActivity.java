package com.jiujiu.ecdemo.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.jiujiu.ecdemo.common.utils.ECPreferences;
import com.jiujiu.ecdemo.common.view.SettingItem;
import com.jiujiu.ecdemo.common.utils.ECPreferenceSettings;
import com.jiujiu.ecdemo.ui.ECSuperActivity;

public class LoginSettingActivity extends ECSuperActivity implements View.OnClickListener{

    private SettingItem mSettingServerIp;
    private SettingItem mSettingAppkey;
    private SettingItem mSettingToken;

    private final class OnConfigClickListener implements View.OnClickListener {

        private int type;
        public OnConfigClickListener(int type) {
            this.type = type;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginSettingActivity.this , EditConfigureActivity.class);
            intent.putExtra("setting_type" , type);
            startActivityForResult(intent , 0xa);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt, -1, com.jiujiu.ecdemo.R.string.app_server_view, this);
    }

    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.activity_login_setting;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initConfigValue();
    }

    /**
     * 加载页面布局
     */
    private void initView() {

        mSettingServerIp = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_serverIP);
        mSettingAppkey = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_appkey);
        mSettingToken = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.settings_token);

        mSettingServerIp.setOnClickListener(new OnConfigClickListener(SettingsActivity.CONFIG_TYPE_SERVERIP));
        mSettingAppkey.setOnClickListener(new OnConfigClickListener(SettingsActivity.CONFIG_TYPE_APPKEY));
        mSettingToken.setOnClickListener(new OnConfigClickListener(SettingsActivity.CONFIG_TYPE_TOKEN));
        initConfigValue();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;

            default:
                break;
        }
    }
}
