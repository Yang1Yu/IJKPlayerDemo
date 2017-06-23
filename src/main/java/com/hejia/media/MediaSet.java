package com.hejia.media;

import java.util.Comparator;

public class MediaSet implements Comparator<Media> {

	@Override
	public int compare(Media o1, Media o2) {
        String name1 = o1.getTitle();  
        String name2 = o2.getTitle(); 
        if(name1.contains("mp3")&&(name2.contains("mp3")))
        {
        	return 0;
        }
        else if(name1.contains("mp4")&&(name2.contains("mp4")))
        {
        	return 0;
        }
        else if(name1.contains("mp3")&&(name2.contains("mp4")))
		{
        	return -1;
		}
        else
		{
        	return 1;
		}
	}
}
