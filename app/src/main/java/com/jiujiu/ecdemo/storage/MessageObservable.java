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
package com.jiujiu.ecdemo.storage;


public class MessageObservable extends ECObservable<OnMessageChange> {

	/**
	 * 分发数据库改变通知
	 * @param session
	 */
	 public void notifyChanged(String session) {
	        synchronized(mObservers) {
	            for (int i = mObservers.size() - 1; i >= 0; i--) {
	            	if(mObservers.get(i)!=null){
	                mObservers.get(i).onChanged(session);
	            	}
	            }
	        }
	    }
}
