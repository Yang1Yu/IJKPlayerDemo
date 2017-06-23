package com.hejia.util;

public class IsFastDoubleMediaListClickUtil {
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeSub = time - lastClickTime;
		if (0 < timeSub && timeSub < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
