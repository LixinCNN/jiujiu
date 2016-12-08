
package com.jiujiu.ecdemo.common.utils;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;


public class ClipboardUtils {

	public static void copyFromEdit(Context context ,CharSequence label, CharSequence text) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			((android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
				.setPrimaryClip(ClipData.newPlainText(label, text));
			return ;
		}
		
		((android.text.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE)).setText(text);
	}
	

	public static CharSequence pasteToResult(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ClipData clipData = ((android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE))
					.getPrimaryClip();
			if ((clipData == null) || (clipData.getItemCount() <= 0)) {
				return null;
			}

			ClipData.Item item = clipData.getItemAt(0);
			if (item == null) {
				return null;
			}
			return item.getText();
		}
		
		return ((android.text.ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE)).getText();
	}
}
