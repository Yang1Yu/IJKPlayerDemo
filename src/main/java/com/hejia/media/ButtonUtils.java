
package com.hejia.media;

import android.util.Log;

/**
 * @author     mengxiangru
 * @date      2016-11-1---����11:06:50
 * @version   
 * @parameter
 * @since
 * @return
 */

public class ButtonUtils {
	 private static long lastClickTime = 0;
	    private static long DIFF = 500;
	    private static int lastButtonId = -1;

	/**
	 * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		return isFastDoubleClick(-1, DIFF);
	}

	/**
	 * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
	 *
	 * @return
	 */
	public static boolean isFastDoubleClick(int buttonId) {
		return isFastDoubleClick(buttonId, DIFF);
	}

	/**
	 * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
	 *
	 * @param diff
	 * @return
	 */
	public static boolean isFastDoubleClick(int buttonId, long diff) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
			Log.v("isFastDoubleClick", "短时间内按钮多次触发");
			return true;
		}
		lastClickTime = time;
		lastButtonId = buttonId;
		return false;
	}

}

