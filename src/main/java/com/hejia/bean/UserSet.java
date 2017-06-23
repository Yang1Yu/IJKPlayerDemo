package com.hejia.bean;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class UserSet implements Comparator<User> {

	@Override
	public int compare(User lhs, User rhs) {
		Collator instance = Collator.getInstance(Locale.CHINA);
		int num = instance.compare(lhs.getName(), rhs.getName());
		return num == 0 ? new Integer(lhs.getNumber()
				.compareTo(rhs.getNumber())) : num;
	}
}
