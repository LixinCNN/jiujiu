package com.jiujiu.ecdemo.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jiujiu.ecdemo.ui.ActivityTransition;
import com.jiujiu.ecdemo.ui.ECSuperActivity;


@ActivityTransition(2)
public class BaseSearch extends ECSuperActivity implements View.OnClickListener{

    public static final int SEARCH_BY_ID = 1;
    public static final int SEARCH_BY_INDISTINCT_NAME = 2;
    public static final String EXTRA_SEARCH_TYPE = "search_type@yuntongxun.com";
    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.base_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt , -1 , com.jiujiu.ecdemo.R.string.search_group , this);

        final Intent intent = new Intent(this , SearchGroupActivity.class);
        findViewById(com.jiujiu.ecdemo.R.id.search_by_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EXTRA_SEARCH_TYPE , SEARCH_BY_ID);
                startActivity(intent);
            }
        });

        findViewById(com.jiujiu.ecdemo.R.id.search_by_indistinct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(EXTRA_SEARCH_TYPE , SEARCH_BY_INDISTINCT_NAME);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
        }
    }
}
