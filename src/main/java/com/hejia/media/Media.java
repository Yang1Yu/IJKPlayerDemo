
package com.hejia.media;


/**
 * @author     mengxiangru
 * @date	2016-10-29---上午10:19:58
 * @version   
 * @parameter
 * @since
 * @return
 */

public class Media {
	private int id = 0;
	private String title = "";
	private long time;
	private String url;

	public Media(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Media(int a,  String c, String url) {
		this.id = a;
		this.title = c;
		this.url = url;

	}

//	@Override
//	public boolean equals(Object obj) {
//		if (obj == null)
//			return false;
//		if (this == obj)
//			return true;
//		if (obj instanceof Media) {
//			Media musicitem = (Media) obj;
//			if (musicitem.getTitle().equals(this.title)) {
//				return true; // ֻ�Ƚ�title
//			}
//
//		}
//		return false;
//	}
//
//	
//	@Override
//	public int hashCode() {
//		return title.hashCode();
//	}

}

