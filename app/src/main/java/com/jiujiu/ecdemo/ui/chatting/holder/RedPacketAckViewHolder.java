package com.jiujiu.ecdemo.ui.chatting.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;


public class RedPacketAckViewHolder extends BaseHolder {

    public View chattingContent;
    public TextView tvMsg;
    /**
     * TextView that display IMessage description.
     */
    /**
     * @param type
     */
    public RedPacketAckViewHolder(int type) {
        super(type);

    }

    public BaseHolder initBaseHolder(View baseView, boolean receive) {
        super.initBaseHolder(baseView);
        chattingTime = (TextView) baseView.findViewById(com.jiujiu.ecdemo.R.id.chatting_time_tv);
        chattingUser = (TextView) baseView.findViewById(com.jiujiu.ecdemo.R.id.chatting_user_tv);
        checkBox = (CheckBox) baseView.findViewById(com.jiujiu.ecdemo.R.id.chatting_checkbox);
        chattingMaskView = baseView.findViewById(com.jiujiu.ecdemo.R.id.chatting_maskview);
        chattingContent = baseView.findViewById(com.jiujiu.ecdemo.R.id.chatting_content_area);
        tvMsg = (TextView) baseView.findViewById(com.jiujiu.ecdemo.R.id.tv_money_msg);
        if (receive) {
            type = 18;
        } else {
            type = 19;
        }
        return this;
    }

    /**
     * @return
     */
    public TextView getRedPacketAckMsgTv() {
        if (tvMsg == null) {
            tvMsg = (TextView) getBaseView().findViewById(com.jiujiu.ecdemo.R.id.tv_money_greeting);
        }
        return tvMsg;
    }

    /**
     * @return
     */
    public ProgressBar getUploadProgressBar() {
        if (progressBar == null) {
            progressBar = (ProgressBar) getBaseView().findViewById(com.jiujiu.ecdemo.R.id.uploading_pb);
        }
        return progressBar;
    }

    @Override
    public TextView getReadTv() {
        return new TextView(CCPAppManager.getContext());
    }

}

