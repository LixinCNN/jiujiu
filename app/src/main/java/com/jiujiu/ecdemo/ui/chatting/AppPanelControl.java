/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.jiujiu.ecdemo.ui.chatting;

import android.content.Context;

import com.jiujiu.ecdemo.common.CCPAppManager;
import com.jiujiu.ecdemo.ui.SDKCoreHelper;
import com.jiujiu.ecdemo.ECApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天插件功能控制器
 */
public class AppPanelControl {

	private Context mContext;

	private static boolean isShowVoipCall = true;
	public int[] cap = new int[]{com.jiujiu.ecdemo.R.string.app_panel_pic,
			com.jiujiu.ecdemo.R.string.app_panel_tackpic, com.jiujiu.ecdemo.R.string.attach_red_packet, com.jiujiu.ecdemo.R.string.app_panel_file, com.jiujiu.ecdemo.R.string.app_panel_location};
	public int[] capVoip = new int[]{com.jiujiu.ecdemo.R.string.app_panel_pic,
			com.jiujiu.ecdemo.R.string.app_panel_tackpic, com.jiujiu.ecdemo.R.string.attach_red_packet, com.jiujiu.ecdemo.R.string.app_panel_file,
			com.jiujiu.ecdemo.R.string.app_panel_voice, com.jiujiu.ecdemo.R.string.app_panel_video, com.jiujiu.ecdemo.R.string.app_panel_read_after_fire, com.jiujiu.ecdemo.R.string.app_panel_location};

	/**
	 *
	 */
	public AppPanelControl() {
		mContext = CCPAppManager.getContext();
	}


	public static void setShowVoipCall(boolean isShowVoipCall) {
		AppPanelControl.isShowVoipCall = isShowVoipCall;
	}


	/**
	 * @return
	 */
	public List<Capability> getCapability() {
		List<Capability> capabilities = new ArrayList<Capability>();

		if (isShowVoipCall && SDKCoreHelper.getInstance().isSupportMedia()) {
			for (int i = 0; i < capVoip.length; i++) {
				Capability capability = getCapability(capVoip[i]);
				capabilities.add(capabilities.size(), capability);
			}

		} else {
			for (int i = 0; i < cap.length; i++) {
				Capability capability = getCapability(cap[i]);
				capabilities.add(capabilities.size(), capability);
			}
		}
		return capabilities;
	}

	/**
	 * @param resid
	 * @return
	 */
	private Capability getCapability(int resid) {
		Capability capability = null;
		switch (resid) {
			case com.jiujiu.ecdemo.R.string.app_panel_pic:
				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_pic), com.jiujiu.ecdemo.R.drawable.image_icon);
				break;
			case com.jiujiu.ecdemo.R.string.app_panel_tackpic:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_tackpic), com.jiujiu.ecdemo.R.drawable.photograph_icon);
				break;
			case com.jiujiu.ecdemo.R.string.app_panel_file:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_file), com.jiujiu.ecdemo.R.drawable.capability_file_icon);
				break;
			case com.jiujiu.ecdemo.R.string.app_panel_voice:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_voice), com.jiujiu.ecdemo.R.drawable.voip_call);
				break;
			case com.jiujiu.ecdemo.R.string.app_panel_video:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_video), com.jiujiu.ecdemo.R.drawable.video_call);
				break;
			case com.jiujiu.ecdemo.R.string.app_panel_read_after_fire:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_read_after_fire), com.jiujiu.ecdemo.R.drawable.fire_msg);
				break;
			case com.jiujiu.ecdemo.R.string.app_panel_location:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.app_panel_location), com.jiujiu.ecdemo.R.drawable.chat_location_normal);
				break;
			//红包按钮
			case com.jiujiu.ecdemo.R.string.attach_red_packet:

				capability = new Capability(getContext().getString(
						com.jiujiu.ecdemo.R.string.attach_red_packet), com.jiujiu.ecdemo.R.drawable.ytx_chat_redpacket_selector);
				break;

			default:
				break;
		}
		capability.setId(resid);
		return capability;
	}

	/**
	 * @return
	 */
	private Context getContext() {
		if (mContext == null) {
			mContext = ECApplication.getInstance().getApplicationContext();
		}
		return mContext;
	}
}
