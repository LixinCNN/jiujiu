package com.jiujiu.ecdemo.ui.contact;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiujiu.ecdemo.common.dialog.ECAlertDialog;
import com.jiujiu.ecdemo.common.utils.BitmapUtil;
import com.jiujiu.ecdemo.common.utils.CheckUtil;
import com.jiujiu.ecdemo.common.utils.ToastUtil;
import com.jiujiu.ecdemo.core.ECAsyncTask;
import com.jiujiu.ecdemo.pojo.ForwardObjectBean;
import com.jiujiu.ecdemo.pojo.RichTextBean;
import com.jiujiu.ecdemo.storage.ContactSqlManager;
import com.jiujiu.ecdemo.storage.GroupSqlManager;
import com.jiujiu.ecdemo.ui.ECSuperActivity;
import com.jiujiu.ecdemo.ui.chatting.IMChattingHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 转发选择联系人
 */
public class SelectContactUI extends ECSuperActivity implements View.OnClickListener {
    /**
     * The sub Activity implement, set the Ui Layout
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return com.jiujiu.ecdemo.R.layout.ec_select_sharecontact_list;
    }

    private  ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTopBarView().setTopBarToStatus(1, com.jiujiu.ecdemo.R.drawable.topbar_back_bt,
                -1, null, null,
                "选择转发对象", null, this);
          initView();
          url=  getIntent().getStringExtra("url");



    }
    private ForwardObjectBean forwardObjectBean;
    private AdapterView.OnItemClickListener clickListener =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              forwardObjectBean  =  adapter.getItem(position);
              messageTo=forwardObjectBean.getUserId();

                doShareMessage(forwardObjectBean);




        }
    };


    private void doShareMessage(final ForwardObjectBean bean) {
        String msg="确定要转发吗";
        ECAlertDialog buildAlert = ECAlertDialog.buildAlert(this, msg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    showCommonProcessDialog("正在转发中...");
                    new ParseHtmlTask(SelectContactUI.this).execute();

                    }
                });


        buildAlert.setTitle(com.jiujiu.ecdemo.R.string.app_tip);
        buildAlert.show();
    }
    private String url;
    private String messageTo;


    Document doc = null;


    private class ParseHtmlTask extends ECAsyncTask {


        public ParseHtmlTask(Context context) {
            super(context);
        }


        @Override
        protected Object doInBackground(Object[] params) {

            try {
                doc = Jsoup.connect(url).timeout(10000).get();

//                String htmlString = getHtmlString(url);
//                doc = Jsoup.parse(htmlString);

                richTextBean=new RichTextBean();
                richTextBean.setUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (doc != null) {
                getShareBean(doc);
            }


            return richTextBean;
        }




        @Override
        protected void onPostExecute(Object o) {
            dismissCommonPostingDialog();
            if(o!=null&&o instanceof RichTextBean){
                RichTextBean bean =(RichTextBean)o;
                handleSendMessage(bean);
            }
        }
    }

    private void handleSendMessage(final RichTextBean bean) {
        ImageLoader.getInstance().loadImage(bean.getPicUrl(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

                ToastUtil.showMessage("转发成功");
                IMChattingHelper.getInstance().handleSendRichTextMessage(bean, messageTo);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                ToastUtil.showMessage("转发成功");
                BitmapUtil.saveBitmapToLocalSDCard(bitmap, bean.getPicUrl());
                IMChattingHelper.getInstance().handleSendRichTextMessage(bean, messageTo);

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


    }




    private  ForwardContactAdapter adapter;

    private void initView() {
        lv= (ListView) findViewById(com.jiujiu.ecdemo.R.id.lv_share_contact);

           ArrayList<ForwardObjectBean> list =getAll();
        adapter=new ForwardContactAdapter(this,0,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(clickListener);

    }


    public String getHtmlString(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection ucon = url.openConnection();
            InputStream instr = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(instr);
            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            return EncodingUtils.getString(baf.toByteArray(), "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }

    RichTextBean richTextBean;
    private void getShareBean(Document doc) {

        if (null != doc) {
            Elements element = doc.select("[src]");

            if(element!=null&&element.size()>0) {
                for (Element src : element) {
                    if (src.tagName().equals("img")) {
                        String imgUrl = src.attr("abs:src");
                        if (CheckUtil.isVailUrl(imgUrl)) {
                            richTextBean.setPicUrl(imgUrl);
                            break;
                        }
                    }
                }
            }
            Elements elementMeta = doc.getElementsByTag("meta");

            Elements titleArr = doc.getElementsByTag("title");

            if(titleArr!=null&&titleArr.size()>0){
                String title = doc.getElementsByTag("title").first().text();
                richTextBean.setTitle(title);
            }else {
                richTextBean.setTitle("标题");
            }

            for(Element s :elementMeta){
                if(s!=null) {
                    if (s.hasAttr("name") && s.attr("name").equals("Description")) {
                        richTextBean.setDesc(s.attr("content"));
                        break;
                    }
                }
            }

//            for (Element str : element) {
//                Elements linkText = str.select("[src]");
//                String imgUrl = linkText.attr("src");
//                if(CheckUtil.isVailUrl(imgUrl)){
//                    richTextBean.setPicUrl(imgUrl);
//                    break;
//                }
//            }
         }

        }



    private ArrayList<ForwardObjectBean> getAll(){

        ArrayList<ForwardObjectBean> groupList =   GroupSqlManager.getForwardGroup();
        ArrayList<ForwardObjectBean> contactList = ContactSqlManager.getForwardContact();
         ArrayList<ForwardObjectBean> list =new ArrayList<ForwardObjectBean>();
        if(groupList!=null) {
            list.addAll(groupList);
        }
        if(contactList!=null) {
            list.addAll(contactList);
        }
        return  list;

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case com.jiujiu.ecdemo.R.id.btn_left:
                hideSoftKeyboard();
                finish();
                break;
        }

    }


    public class ForwardContactAdapter extends ArrayAdapter<ForwardObjectBean> {


        public ForwardContactAdapter(Context context, int textViewResourceId, List<ForwardObjectBean> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ForwardShareHolder holder;
            if (convertView == null || convertView.getTag() == null) {
                view = getLayoutInflater().inflate(com.jiujiu.ecdemo.R.layout.select_contact_item, null);
                holder = new ForwardShareHolder();
                view.setTag(holder);

                holder.name = (TextView) view.findViewById(com.jiujiu.ecdemo.R.id.contact_name);
                holder.iv=(ImageView)view.findViewById(com.jiujiu.ecdemo.R.id.contactitem_avatar_iv);

            } else {
                view = convertView;
                holder = (ForwardShareHolder) convertView.getTag();
            }

            ForwardObjectBean bean = getItem(position);
            if (bean != null) {
                holder.name.setText(bean.getUserName());
                String userid = bean.getUserId();
                if(!TextUtils.isEmpty(userid)){
                    if(userid.startsWith("g")){
                        holder.iv.setImageResource(com.jiujiu.ecdemo.R.drawable.group_head);
                    }
                }
            }

            return view;

        }

        class ForwardShareHolder {
            TextView name;
            ImageView iv;
        }
    }

}
