package com.jiujiu.ecdemo.ui.group;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jiujiu.ecdemo.common.dialog.ECAlertDialog;
import com.jiujiu.ecdemo.common.utils.ToastUtil;
import com.jiujiu.ecdemo.common.view.SettingItem;
import com.jiujiu.ecdemo.common.CCPAppManager;
import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.im.ECGroupMember;

public class GroupMemberCardActivity extends ECSuperActivity implements OnClickListener, GroupService.GroupCardCallBack {

	private SettingItem mGroupIdItem;
	private SettingItem mGroupUserIdItem;
	private SettingItem mGroupGenderItem;
	private SettingItem mGroupNameItem;
	private SettingItem mGroupTelItem;
	private SettingItem mGroupEmailItem;
	private SettingItem mGroupRemarkItem;
	private String groupId;


	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		
		return com.jiujiu.ecdemo.R.layout.group_card_info;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt,
	                com.jiujiu.ecdemo.R.drawable.btn_style_green, null,
	                getString(com.jiujiu.ecdemo.R.string.dialog_ok_button),
	                getString(com.jiujiu.ecdemo.R.string.group_card), null, this);
		 
		 initViews();
		 groupId = getIntent().getStringExtra("groupId");
		 showCommonProcessDialog("");
		 GroupService.queryGroupCard(CCPAppManager.getUserId(), groupId, this);
	}


	private void initViews() {

	mGroupIdItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_id);
	mGroupUserIdItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_userid);
	mGroupGenderItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_gender);
	mGroupNameItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_nickname);
	mGroupTelItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_tel);
	mGroupEmailItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_email);
	mGroupRemarkItem = (SettingItem) findViewById(com.jiujiu.ecdemo.R.id.group_card_remark);
	
	
	mGroupNameItem.setOnClickListener(this);
	mGroupTelItem.setOnClickListener(this);
	mGroupEmailItem.setOnClickListener(this);
	mGroupRemarkItem.setOnClickListener(this);
		
		
	}


	@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
            case com.jiujiu.ecdemo.R.id.group_card_nickname:
                
            	onButtonClick(com.jiujiu.ecdemo.R.id.group_card_nickname);
                break;
            case com.jiujiu.ecdemo.R.id.group_card_tel:
            	onButtonClick(com.jiujiu.ecdemo.R.id.group_card_tel);
            	
            	break;
            case com.jiujiu.ecdemo.R.id.group_card_email:
            	onButtonClick(com.jiujiu.ecdemo.R.id.group_card_email);
            	
            	break;
            case com.jiujiu.ecdemo.R.id.group_card_remark:
            	onButtonClick(com.jiujiu.ecdemo.R.id.group_card_remark);
            	break;
            case com.jiujiu.ecdemo.R.id.text_right:
            	
            	ECGroupMember groupMember =new ECGroupMember();
            	groupMember.setBelong(groupId);
            	groupMember.setDisplayName(mGroupNameItem.getCheckedTextView().getText().toString().trim());
            	groupMember.setEmail(mGroupEmailItem.getCheckedTextView().getText().toString().trim());
            	groupMember.setRemark(mGroupRemarkItem.getCheckedTextView().getText().toString().trim());
            	groupMember.setTel(mGroupTelItem.getCheckedTextView().getText().toString().trim());
            	groupMember.setVoipAccount(CCPAppManager.getUserId());
            	
            	showCommonProcessDialog("");
            	GroupService.modifyGroupCard(groupMember, this);
            	
            	break;
            default:
                break;
        }
    }
	
	private void onButtonClick(final int id) {
		showInputCodeDialog(getString(com.jiujiu.ecdemo.R.string.group_card_edit), id);
	}


	@Override
	public void onQueryGroupCardSuccess(ECGroupMember member) {
		
		dismissCommonPostingDialog();
		if(member!=null){
			mGroupIdItem.setCheckText(groupId);
			mGroupUserIdItem.setCheckText(member.getVoipAccount());
			mGroupGenderItem.setCheckText(member.getSex()==0?"女":"男");
			mGroupNameItem.setCheckText(member.getDisplayName());
			mGroupTelItem.setCheckText(member.getTel());
			mGroupEmailItem.setCheckText(member.getEmail());
			mGroupRemarkItem.setCheckText(member.getRemark());
		}
	}


	
	
	protected void showInputCodeDialog(String message,final int buttonId) {
        View view = View.inflate(this , com.jiujiu.ecdemo.R.layout.dialog_edit_context , null);
        final EditText editText = (EditText) view.findViewById(com.jiujiu.ecdemo.R.id.sendrequest_content);
        ((TextView) view.findViewById(com.jiujiu.ecdemo.R.id.sendrequest_tip)).setText(message);
        ECAlertDialog dialog = ECAlertDialog.buildAlert(this, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	String searchC = editText.getText().toString();

				if (!TextUtils.isEmpty(searchC)) {

					doAction(buttonId, searchC);
				}
            }
        });
        dialog.setContentView(view);
        dialog.setTitle("");
        dialog.show();
    }


	protected void doAction(int buttonId, String searchC) {

		
		switch (buttonId) {
		  case com.jiujiu.ecdemo.R.id.group_card_nickname:
              
			  mGroupNameItem.setCheckText(searchC);
          	
              break;
          case com.jiujiu.ecdemo.R.id.group_card_tel:
          	
        	  mGroupTelItem.setCheckText(searchC);
          	
          	break;
          case com.jiujiu.ecdemo.R.id.group_card_email:
        	  mGroupEmailItem.setCheckText(searchC);
          	
          	
          	break;
          case com.jiujiu.ecdemo.R.id.group_card_remark:
          	
        	  mGroupRemarkItem.setCheckText(searchC);
          	
          	break;

		default:
			break;
		}
	}


	


	@Override
	public void onModifyGroupCardFailed(ECError error) {
		
		dismissCommonPostingDialog();
		ToastUtil.showMessage("修改群聊名片失败[" + error.errorCode + "]");
	}


	@Override
	public void onQueryGroupCardFailed(ECError error) {
		// TODO Auto-generated method stub
		dismissCommonPostingDialog();
		ToastUtil.showMessage("查询群聊名片失败[" + error.errorCode + "]");
		
	}


	@Override
	public void onModifyGroupCardSuccess() {
		// TODO Auto-generated method stub
		dismissCommonPostingDialog();
		ToastUtil.showMessage("修改群聊名片成功");
		finish();
		
	}
	

}
