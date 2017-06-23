
package com.hejia.media;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author     mengxiangru
 * @date       2016-10-29---下午2:10:24
 * @version   
 * @parameter
 * @since
 * @return
 */

public class MyTextView extends TextView {

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public MyTextView(Context context) {
		super(context);

	}
	
	
	@Override
	public boolean isFocused() {

		return true;
	}
}

