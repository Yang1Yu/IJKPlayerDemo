package com.hejia.util;

public class IsFastChangeFragmentUtil {
	private static long lastClickTime;

	public static boolean isFastChangeFragment() {
		long time = System.currentTimeMillis();
		long timeSub = time - lastClickTime;
		if (0 < timeSub && timeSub < 1) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
