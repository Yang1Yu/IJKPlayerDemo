package com.hejia.webcam;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WebcamPreview extends SurfaceView implements SurfaceHolder.Callback, Runnable {
	private static final boolean DEBUG = true;
	private static final String TAG = "TW686xCamera";
	protected Context context;
	private SurfaceHolder holder;
	Thread mainLoop = null;
	private Bitmap bmp = null;

	private boolean cameraExists = false;
	// 以防隐藏后再显示时画面重复
	public static int cameraId = 0;/* 0~3为有效值 */

	static final int IMG_WIDTH = 720;// 720;
	static final int IMG_HEIGHT = 480;// 576;
	static final int IMG_WIDTH_DIV2 = 360;// 720;
	static final int IMG_HEIGHT_DIV2 = 240;// 576;

	private Rect mViewWindow;
	private int CameraFd = -1;
	private int iRet;
	private String CameraDevName;

	private int iPreViewFlag = 0;

	public native int startCamera(int iCameraId, int width, int height);

	/*preview_flag 0:不预览，1:预览1/2/尺寸，2:预览全尺寸*/
	public native int loadNextFrame(int fd, int PreViewFlag, Bitmap bitmap);

	public native void stopCamera(int fd);

	static {
		if (DEBUG)
			Log.d(TAG, "loadLibrary");
		System.loadLibrary("webcam");
	}

	public WebcamPreview(Context context) {
		super(context);
		init();
	}

	public WebcamPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		Log.d(TAG, "WebcamPreview constructed");
		setFocusable(true);

		holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void run() {
		while (true && cameraExists) {
			synchronized (this) {
				/*大于0时执行预览*/
				if (iPreViewFlag > 0 && loadNextFrame(CameraFd, iPreViewFlag, bmp) == 0) {
					Canvas canvas = getHolder().lockCanvas();
					if (canvas != null) {
						if ((canvas.getWidth() <= 384) && (iPreViewFlag == 1)) {
							canvas.drawBitmap(bmp, null, mViewWindow, null);
						} else if ((canvas.getWidth() > 384) && (iPreViewFlag == 2)) {
							canvas.drawBitmap(bmp, null, mViewWindow, null);
						}
						getHolder().unlockCanvasAndPost(canvas);
					} else if (iPreViewFlag == 3) {
						iPreViewFlag = 0;
					}

				}
			}
			try {
				Thread.sleep(40);
			} catch (Exception e) {
			}
		}

		if (bmp != null && !bmp.isRecycled()) {
			bmp.recycle();
			bmp = null;
		}
		bmp = null;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		/* 打开成功一次后不会再关闭，所以一旦camerafd为有效值，即已经成功打开过 */
		if (-1 == CameraFd) {
			CameraFd = startCamera(cameraId, IMG_WIDTH, IMG_HEIGHT);

			if (CameraFd != -1)
				cameraExists = true;

			mainLoop = new Thread(this);
			mainLoop.start();
		}
		cameraId++;

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int winWidth, int winHeight) {
		if (DEBUG) {
			Log.i("chanyeol", "surfaceChange" + winWidth);
		}
		if (!cameraExists)
			return;
		synchronized (this) {
			if (bmp != null && !bmp.isRecycled()) {
				bmp.recycle();
			}
			/* 宽为768的分辨率屏，大于一半的宽度代表全屏显示 */
			if (winWidth > 384) {
				bmp = Bitmap.createBitmap(IMG_WIDTH, IMG_HEIGHT, Bitmap.Config.RGB_565);
				/* 正常分辨率预览 */
				iPreViewFlag = 2;
			} else {
				bmp = Bitmap.createBitmap(IMG_WIDTH_DIV2, IMG_HEIGHT_DIV2, Bitmap.Config.RGB_565);
				/* 长宽各1/2分辨率预览 */
				iPreViewFlag = 1;
			}
			int width, height, dw, dh;
			dw = 0;
			dh = 0;
			if (winWidth * 3 / 4 <= winHeight) {
				dw = 0;
				dh = (winHeight - winWidth * 3 / 4) / 2;
				width = dw + winWidth - 1;
				height = dh + winWidth * 3 / 4 - 1;
			} else {
				dw = (winWidth - winHeight * 4 / 3) / 2;
				dh = 0;
				width = dw + winHeight * 4 / 3 - 1;
				height = dh + winHeight - 1;
			}
			mViewWindow = new Rect(dw, dh, width, height);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		if (!cameraExists) {
			cameraId = 0;
			return;
		}
		synchronized (this) {
			/* 只是保留线程，不执行预览，且要清空jni视频采集队列 */
			iPreViewFlag = 3;
//			stopCamera(CameraFd);
//			cameraExists = false;
//
//			CameraFd = -1;

		}
		cameraId = 0;
	}
}
