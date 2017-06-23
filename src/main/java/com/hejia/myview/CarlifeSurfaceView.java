package com.hejia.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by yining on 2017/5/26.
 */
public class CarlifeSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
	private SurfaceHolder holder;
	private static final String TAG = "CarLifeVideoJni";
	private int SurfaceWidth;
	private int SurfaceHeight;

	// JNI functions
	public static native int start(Object surface, int width, int height);
	public static native int stop();
	static {
		Log.d(TAG, "load carlife_video_jni Library");
		System.loadLibrary("carlife_video_jni");
	}

	public CarlifeSurfaceView(Context context) {
		super(context);
		init();
	}

	public CarlifeSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CarlifeSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setFocusable(true);
		holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		synchronized (this) {// 同步锁
			SurfaceWidth  = width;
			SurfaceHeight = height;
			start(holder.getSurface(),SurfaceWidth, SurfaceHeight);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		synchronized (this) {// 同步锁
			stop();
		}
	}

}
