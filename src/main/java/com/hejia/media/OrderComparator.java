package com.hejia.media;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class OrderComparator implements Comparator<Media> {
	@Override
	public int compare(Media o1, Media o2) {
		Collator instance = Collator.getInstance(Locale.CHINA);
		int num;
		if (o1.getTitle().contains("mp3") && (o2.getTitle().contains("mp3"))) {
			num = instance.compare(o1.getTitle(), o2.getTitle());
			return num == 0 ? new Integer(o1.getUrl()
					.compareTo(o2.getUrl())) : num;

		} else if (o1.getTitle().contains("mp4") && (o2.getTitle().contains("mp4"))) {
			num = instance.compare(o1.getTitle(), o2.getTitle());
			return num == 0 ? new Integer(o1.getUrl()
					.compareTo(o2.getUrl())) : num;
		} else if (o1.getTitle().contains("mp3") && (o2.getTitle().contains("mp4"))) {
			return -1;
		} else {
			return 1;
		}

	}
}