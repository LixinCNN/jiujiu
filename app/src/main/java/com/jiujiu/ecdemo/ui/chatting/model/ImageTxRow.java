/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.cloopen.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jiujiu.ecdemo.ui.chatting.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jiujiu.ecdemo.common.utils.DemoUtils;
import com.jiujiu.ecdemo.common.utils.FileAccessor;
import com.jiujiu.ecdemo.common.utils.ResourceHelper;
import com.jiujiu.ecdemo.ui.chatting.ChattingActivity;
import com.jiujiu.ecdemo.ui.chatting.holder.BaseHolder;
import com.jiujiu.ecdemo.ui.chatting.holder.ImageRowViewHolder;
import com.jiujiu.ecdemo.ui.chatting.view.ChattingItemContainer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;


public class ImageTxRow extends BaseChattingRow {

	public ImageTxRow(int type) {
		super(type);
	}

	@Override
	public View buildChatView(LayoutInflater inflater, View convertView) {
		// we have a don't have a converView so we'll have to create a new one
		if (convertView == null) {
			convertView = new ChattingItemContainer(inflater,
					com.jiujiu.ecdemo.R.layout.chatting_item_to_picture);

			// use the view holder pattern to save of already looked up subviews
			ImageRowViewHolder holder = new ImageRowViewHolder(mRowType);
			convertView.setTag(holder.initBaseHolder(convertView, false));

		}
		return convertView;
	}

	@Override
	public void buildChattingData(Context context, BaseHolder baseHolder,
			ECMessage detail, int position) {

		String isRead = IMessageSqlManager.getMsgReadStatus(detail.getMsgId());
		boolean isFireMsg = IMessageSqlManager.isFireMsg(detail.getMsgId());
		ImageRowViewHolder holder = (ImageRowViewHolder) baseHolder;
		ECFileMessageBody body = (ECFileMessageBody) detail.getBody();
		String userData = detail.getUserData();
		if (TextUtils.isEmpty(userData)) {
			return;
		}
		int start = userData.indexOf("THUMBNAIL://");
		int gif = userData.indexOf(",PICGIF://");
		boolean isGif = userData.contains("PICGIF://true");
		;
		if (start != -1) {
			String thumbnail;
			if (gif == -1) {
				thumbnail = userData.substring(start);
			} else {
				thumbnail = userData.substring(start, gif);
			}
			ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(
					detail.getMsgId());
			Bitmap thumbBitmap = ImgInfoSqlManager.getInstance()
					.getThumbBitmap(thumbnail, 2);
			if (imgInfo != null && !TextUtils.isEmpty(imgInfo.getBigImgPath())) {
				if (!isGif) {
					isGif = imgInfo.getBigImgPath().endsWith(".gif");
				}
				String uri = "file://" + FileAccessor.getImagePathName() + "/"
						+ imgInfo.getBigImgPath();
				DisplayImageOptions.Builder optionsBuilder = DemoUtils
						.getChatDisplayImageOptionsBuilder();
				optionsBuilder.showImageOnLoading(new BitmapDrawable(
						thumbBitmap));
				if (isFireMsg) {
					if ("1".equals(isRead)) {
						ImageLoader.getInstance().displayImage("assets://msg_fire_readed.png", holder.chattingContentIv);
					} else {
						ImageLoader.getInstance().displayImage(uri,holder.chattingContentIv, optionsBuilder.build());
					}
				} else {
					ImageLoader.getInstance().displayImage(uri,holder.chattingContentIv, optionsBuilder.build());
				}
			} else {
				if (isFireMsg) {
					if ("1".equals(isRead)) {
						ImageLoader.getInstance().displayImage("assets://msg_fire_readed.png", holder.chattingContentIv);
					} else {
						holder.chattingContentIv.setImageBitmap(thumbBitmap);
					}
				} else {
					holder.chattingContentIv.setImageBitmap(thumbBitmap);
				}
			}
		} else {
			holder.chattingContentIv.setImageBitmap(null);
		}
		ViewHolderTag holderTag = ViewHolderTag.createTag(detail,
				ViewHolderTag.TagType.TAG_VIEW_PICTURE, position);
		OnClickListener onClickListener = ((ChattingActivity) context).mChattingFragment
				.getChattingAdapter().getOnClickListener();
		holder.chattingContentIv.setTag(holderTag);
		holder.chattingContentIv.setOnClickListener(onClickListener);
		if (isFireMsg && "1".equals(isRead)) {
			holder.chattingContentIv.setOnClickListener(null);
		}
		getMsgStateResId(position, holder, detail, onClickListener);
		if (isGif) {
			boolean showGif = detail.getMsgStatus() == ECMessage.MessageStatus.SUCCESS
					|| detail.getMsgStatus() == ECMessage.MessageStatus.RECEIVE;
			if (holder.mGifIcon != null) {
				holder.mGifIcon.setVisibility(!showGif ? View.GONE
						: View.VISIBLE);
			}
		} else {
			holder.mGifIcon.setVisibility(View.GONE);
		}/* else { */
		int startWidth = userData.indexOf("outWidth://");
		int startHeight = userData.indexOf(",outHeight://");
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.chattingContentIv
				.getLayoutParams();
		if (startWidth != -1 && startHeight != -1 && start != -1) {
			int imageMinWidth = /* DemoUtils.getImageMinWidth(context) */ResourceHelper
					.fromDPToPix(context, isGif ? 200 : 100);
			int width = DemoUtils.getInt(userData.substring(startWidth
					+ "outWidth://".length(), startHeight), imageMinWidth);
			int height = DemoUtils.getInt(userData.substring(startHeight
					+ ",outHeight://".length(), start - 1), imageMinWidth);
			holder.chattingContentIv.setMinimumWidth(imageMinWidth);
			params.width = imageMinWidth;
			int _height = height * imageMinWidth / width;
			if (_height > ResourceHelper.fromDPToPix(context, 230)) {
				_height = ResourceHelper.fromDPToPix(context, 230);
				holder.chattingContentIv
						.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}
			if (width != 0) {
				holder.chattingContentIv.setMinimumHeight(_height);
				params.height = _height;
			} else {
				holder.chattingContentIv.setMinimumHeight(imageMinWidth);
				params.height = imageMinWidth;
			}
			holder.chattingContentIv.invalidate();
			holder.chattingContentIv.setLayoutParams(params);
		}
		// }
	}

	@Override
	public int getChatViewType() {
		return ChattingRowType.IMAGE_ROW_TRANSMIT.ordinal();
	}

	@Override
	public boolean onCreateRowContextMenu(ContextMenu contextMenu,
			View targetView, ECMessage detail) {
		// TODO Auto-generated method stub
		return false;
	}

}
