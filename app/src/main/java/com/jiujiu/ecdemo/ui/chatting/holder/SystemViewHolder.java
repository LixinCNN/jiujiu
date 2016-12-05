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
package com.jiujiu.ecdemo.ui.chatting.holder;

import android.widget.TextView;


public class SystemViewHolder extends BaseHolder {

	public TextView mSystemView;
	/**
	 * @param type
	 */
	public SystemViewHolder(int type) {
		super(type);
	}

	@Override
	public TextView getReadTv() {
		return null;
	}

}