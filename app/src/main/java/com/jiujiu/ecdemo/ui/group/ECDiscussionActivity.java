package com.jiujiu.ecdemo.ui.group;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.jiujiu.ecdemo.ui.GroupListFragment;

public class ECDiscussionActivity extends ECSuperActivity implements OnClickListener {
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt, -1, "讨论组列表", this);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return com.jiujiu.ecdemo.R.layout.discussion_activity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
        case com.jiujiu.ecdemo.R.id.btn_left:
            hideSoftKeyboard();
            finish();
            GroupListFragment.sync=false;
            break;
        
        default:
            break;
    }
		
	}

	

}
