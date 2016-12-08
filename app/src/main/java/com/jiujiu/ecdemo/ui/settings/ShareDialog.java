package com.jiujiu.ecdemo.ui.settings;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareDialog {

    private AlertDialog dialog;
    private GridView gridView;
    private RelativeLayout cancelButton;
    private SimpleAdapter saImageItems;
    private int[] image = {com.jiujiu.ecdemo.R.drawable.share_wechat, com.jiujiu.ecdemo.R.drawable.share_wechat_moments};
    private String[] name = { "微信好友", "微信朋友圈"};

    public ShareDialog(Context context) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
        window.setContentView(com.jiujiu.ecdemo.R.layout.share_dialog);
        gridView = (GridView) window.findViewById(com.jiujiu.ecdemo.R.id.share_gridView);
        cancelButton = (RelativeLayout) window.findViewById(com.jiujiu.ecdemo.R.id.share_cancel);
        List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", image[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            shareList.add(map);
        }

        saImageItems = new SimpleAdapter(context, shareList, com.jiujiu.ecdemo.R.layout.share_item, new String[]{"ItemImage", "ItemText"}, new int[]{com.jiujiu.ecdemo.R.id.imageView1, com.jiujiu.ecdemo.R.id.textView1});
        gridView.setAdapter(saImageItems);
    }

    public void setCancelButtonOnClickListener(OnClickListener Listener) {
        cancelButton.setOnClickListener(Listener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}