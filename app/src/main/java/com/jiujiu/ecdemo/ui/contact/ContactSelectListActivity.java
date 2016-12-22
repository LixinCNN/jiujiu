package com.jiujiu.ecdemo.ui.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import com.jiujiu.ecdemo.common.dialog.ECProgressDialog;
import com.jiujiu.ecdemo.common.utils.DemoUtils;
import com.jiujiu.ecdemo.common.utils.LogUtil;
import com.jiujiu.ecdemo.common.view.TopBarView;
import com.jiujiu.ecdemo.storage.ContactSqlManager;
import com.jiujiu.ecdemo.storage.GroupMemberSqlManager;
import com.jiujiu.ecdemo.ui.ContactListFragment;
import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.jiujiu.ecdemo.ui.chatting.ChattingActivity;
import com.jiujiu.ecdemo.ui.chatting.ChattingFragment;
import com.jiujiu.ecdemo.ui.group.GroupService;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人选择页面
 */
public class ContactSelectListActivity  extends ECSuperActivity implements
        View.OnClickListener  , ContactListFragment.OnContactClickListener {
    private ECProgressDialog mPostingdialog;
    private static final String TAG = "ECSDK_Demo.ContactSelectListActivity";
    /**查看群组*/
    public static final int REQUEST_CODE_VIEW_GROUP_OWN = 0x2a;
    private TopBarView mTopBarView;
    private boolean mNeedResult;
    private boolean mShowGroup;
    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.layout_contact_select;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();

        mNeedResult = getIntent().getBooleanExtra("group_select_need_result", false);
        mShowGroup = getIntent().getBooleanExtra("select_type", true);
        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(com.jiujiu.ecdemo.R.id.contact_container) == null) {
            ContactListFragment list = ContactListFragment.newInstance(mShowGroup ? ContactListFragment.TYPE_SELECT : ContactListFragment.TYPE_NON_GROUP);
            fm.beginTransaction().add(com.jiujiu.ecdemo.R.id.contact_container, list).commit();
        }
        mTopBarView = getTopBarView();
        String actionBtn = getString(com.jiujiu.ecdemo.R.string.radar_ok_count, getString(com.jiujiu.ecdemo.R.string.dialog_ok_button) , 0);
        mTopBarView.setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt, com.jiujiu.ecdemo.R.drawable.btn_style_green, null, actionBtn, getString(com.jiujiu.ecdemo.R.string.select_contacts), null, this);
        mTopBarView.setRightBtnEnable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case com.jiujiu.ecdemo.R.id.text_right:
                List<Fragment> fragments = getSupportFragmentManager().getFragments();

                if(fragments.get(0) instanceof ContactListFragment) {
                    String chatuser = ((ContactListFragment) fragments.get(0) ).getChatuser();
                    String[] split = chatuser.split(",");
                    if(split.length == 1 && !mNeedResult) {
                        Intent intent = new Intent(ContactSelectListActivity.this , ChattingActivity.class);
                        intent.putExtra(ChattingFragment.RECIPIENTS, split[0]);
                        startActivity(intent);
                        finish();
                        return ;
                    }

                    if(mNeedResult) {
                        Intent intent = new Intent();
                        intent.putExtra("Select_Conv_User", split);
                        setResult(-1, intent);
                        finish();
                        return ;
                    }

                    if(split.length > 0) {
                        postCreatePrivateChatroom(split);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 创建一个私有群组
     * @param split
     */
    private void postCreatePrivateChatroom(String[] split) {
        mPostingdialog = new ECProgressDialog(this, com.jiujiu.ecdemo.R.string.create_chatroom_posting);
        mPostingdialog.show();
        GroupService.doCreateGroup(split, new ECGroupManager.OnInviteJoinGroupListener() {


            @Override
            public void onInviteJoinGroupComplete(ECError error, String groupId,
                                                  String[] members) {
                if ("000000".equals(error.errorCode)) {
                    GroupMemberSqlManager.insertGroupMembers(groupId, members);
                    ArrayList<String> contactName = ContactSqlManager.getContactName(members);
                    String users = DemoUtils.listToString(contactName, ",");
                    Intent intent = new Intent(ContactSelectListActivity.this, ChattingActivity.class);
                    intent.putExtra(ChattingFragment.RECIPIENTS, groupId);
                    intent.putExtra(ChattingFragment.CONTACT_USER, users);
                    startActivity(intent);
                }
                dismissPostingDialog();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d(TAG, "onActivityResult: requestCode=" + requestCode
                + ", resultCode=" + resultCode + ", data=" + data);

        // If there's no data (because the user didn't select a picture and
        // just hit BACK, for example), there's nothing to do.
        if (requestCode == REQUEST_CODE_VIEW_GROUP_OWN) {
            if (data == null) {
                return;
            }
        } else if (resultCode != RESULT_OK) {
            LogUtil.d("onActivityResult: bail due to resultCode=" + resultCode);
            return;
        }

        String contactId = data.getStringExtra(ChattingFragment.RECIPIENTS);
        String contactUser = data.getStringExtra(ChattingFragment.CONTACT_USER);
        if(contactId != null && contactId.length() > 0) {
            Intent intent = new Intent(this ,  ChattingActivity.class);
            intent.putExtra(ChattingFragment.RECIPIENTS, contactId);
            intent.putExtra(ChattingFragment.CONTACT_USER, contactUser);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onContactClick(int count) {
        mTopBarView.setRightBtnEnable(count > 0 ? true:false);
        mTopBarView.setRightButtonText(getString(com.jiujiu.ecdemo.R.string.radar_ok_count, getString(com.jiujiu.ecdemo.R.string.dialog_ok_button) , count));
    }

    @Override
    public void onSelectGroupClick() {
        Intent intent = new Intent(this, GroupCardSelectUI.class);
        startActivityForResult(intent ,REQUEST_CODE_VIEW_GROUP_OWN);
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
}