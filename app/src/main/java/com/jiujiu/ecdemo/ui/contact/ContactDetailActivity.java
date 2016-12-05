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
 */package com.jiujiu.ecdemo.ui.contact;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiujiu.ecdemo.ui.chatting.base.EmojiconTextView;
import com.jiujiu.ecdemo.common.CCPAppManager;
import com.jiujiu.ecdemo.common.utils.ToastUtil;
import com.jiujiu.ecdemo.storage.ContactSqlManager;
import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.jiujiu.ecdemo.ui.SDKCoreHelper;

/**联系人详情页面
 * 创建于2016/11/28
 */
public class ContactDetailActivity extends ECSuperActivity implements View.OnClickListener{

    public final static String RAW_ID = "raw_id";
    public final static String MOBILE = "mobile";
    public final static String DISPLAY_NAME = "display_name";

    private ImageView mPhotoView;
    private EmojiconTextView mUsername;
    private TextView mNumber;

    private ECContacts mContacts;

    private View.OnClickListener onClickListener
            = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mContacts == null) {
                return ;
            }
            CCPAppManager.startChattingAction(ContactDetailActivity.this, mContacts.getContactid(), mContacts.getNickname(), true);
            setResult(RESULT_OK);
            finish();
        }
    };

    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.layout_contact_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initActivityState(savedInstanceState);
        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt, -1, com.jiujiu.ecdemo.R.string.contact_contactDetail, this);
    }


    /**
     * @param savedInstanceState
     */
    private void initActivityState(Bundle savedInstanceState) {
        long rawId = getIntent().getLongExtra(RAW_ID, -1);
        if(rawId == -1) {
            String mobile = getIntent().getStringExtra(MOBILE);
            String displayname = getIntent().getStringExtra(DISPLAY_NAME);
            mContacts = ContactSqlManager.getCacheContact(mobile);
            if(mContacts == null) {
                mContacts = new ECContacts(mobile);
                mContacts.setNickname(displayname);
            }
        }

        if(mContacts == null && rawId != -1) {
            mContacts = ContactSqlManager.getContact(rawId);
        }

        if(mContacts == null) {
            ToastUtil.showMessage(com.jiujiu.ecdemo.R.string.contact_none);
            finish();
            return ;
        }

        mPhotoView.setImageBitmap(ContactLogic.getPhoto(mContacts.getRemark()));
        mUsername.setText(TextUtils.isEmpty(mContacts.getNickname()) ?mContacts.getContactid() :mContacts.getNickname());
        mNumber.setText(mContacts.getContactid());
    }


    /**
     *
     */
    private void initView() {
        mPhotoView = (ImageView) findViewById(com.jiujiu.ecdemo.R.id.desc);
        mUsername = (EmojiconTextView) findViewById(com.jiujiu.ecdemo.R.id.contact_nameTv);
        mNumber = (TextView) findViewById(com.jiujiu.ecdemo.R.id.contact_numer);
        findViewById(com.jiujiu.ecdemo.R.id.entrance_chat).setOnClickListener(onClickListener);
        findViewById(com.jiujiu.ecdemo.R.id.entrance_voip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContacts == null) {
                    return ;
                }
                CCPAppManager.showCallMenu(ContactDetailActivity.this ,mContacts.getNickname() , mContacts.getContactid());
            }
        });
        
       if(!SDKCoreHelper.getInstance().isSupportMedia()){
    	   
    	   findViewById(com.jiujiu.ecdemo.R.id.entrance_voip).setVisibility(View.GONE);
       }
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPhotoView != null) {
            mPhotoView.setImageDrawable(null);
        }
        onClickListener = null;
        mContacts = null;

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
